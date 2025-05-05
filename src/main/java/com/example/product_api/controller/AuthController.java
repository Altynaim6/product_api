package com.example.product_api.controller;

import com.example.product_api.exception.CustomException;
import com.example.product_api.model.domain.RefreshToken;
import com.example.product_api.model.domain.User;
import com.example.product_api.model.domain.VerificationToken;
import com.example.product_api.model.dto.auth.AuthRequest;
import com.example.product_api.model.dto.auth.AuthResponse;
import com.example.product_api.model.dto.user.UserRequest;
import com.example.product_api.model.enums.Role;
import com.example.product_api.repository.UserRepository;
import com.example.product_api.security.jwt.JwtTokenProvider;
import com.example.product_api.service.EmailSenderService;
import com.example.product_api.service.RefreshTokenService;
import com.example.product_api.service.VerificationTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final EmailSenderService emailSenderService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("Email is already registered", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setEnabled(false);
        User savedUser = userRepository.save(user);

        VerificationToken token = verificationTokenService.createToken(savedUser);
        String link = "http://localhost:8080/auth/verify?token=" + token.getToken();
        emailSenderService.sendVerificationEmail(savedUser.getEmail(), link);

        return ResponseEntity.ok("Registration successful. Please check your email to verify your account.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = (User) authentication.getPrincipal();

            if (!user.isEnabled()) {
                throw new CustomException("Please verify your email before logging in", HttpStatus.UNAUTHORIZED);
            }

            String accessToken = jwtTokenProvider.generateAccessToken(
                    user.getEmail(),
                    user.getName(),
                    user.getRole().name()
            );

            RefreshToken refreshToken = refreshTokenService.getOrCreateRefreshToken(user);

            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken.getToken()));

        } catch (AuthenticationException ex) {
            throw new CustomException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshTokenStr = request.get("refresh_token");
        if (refreshTokenStr == null || refreshTokenStr.isBlank()) {
            throw new CustomException("Refresh token is missing", HttpStatus.BAD_REQUEST);
        }

        RefreshToken token = refreshTokenService.findByToken(refreshTokenStr)
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new CustomException("Refresh token not found or expired", HttpStatus.UNAUTHORIZED));

        User user = token.getUser();
        String newAccessToken = jwtTokenProvider.generateAccessToken(
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );

        return ResponseEntity.ok(new AuthResponse(newAccessToken, token.getToken()));
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verifyAccount(@RequestParam("token") String token) {
        VerificationToken verificationToken = verificationTokenService.findByToken(token)
                .orElseThrow(() -> new CustomException("Invalid verification token", HttpStatus.BAD_REQUEST));

        if (verificationToken.getExpiryDate().isBefore(Instant.now())) {
            throw new CustomException("Verification token has expired", HttpStatus.BAD_REQUEST);
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        verificationTokenService.deleteByUser(user);

        return ResponseEntity.status(302)
                .header("Location", "/login.html")
                .build();
    }
}