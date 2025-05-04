package com.example.product_api.controller;

import com.example.product_api.exception.CustomException;
import com.example.product_api.model.domain.User;
import com.example.product_api.model.domain.VerificationToken;
import com.example.product_api.model.dto.auth.AuthRequest;
import com.example.product_api.model.dto.auth.AuthResponse;
import com.example.product_api.model.dto.auth.RefreshTokenRequest;
import com.example.product_api.model.dto.auth.RegisterRequest;
import com.example.product_api.repository.UserRepository;
import com.example.product_api.service.impl.AuthServiceImpl;
import com.example.product_api.service.VerificationTokenService;

import io.swagger.v3.oas.annotations.Operation;                             // :contentReference[oaicite:0]{index=0}
import io.swagger.v3.oas.annotations.tags.Tag;                                // :contentReference[oaicite:1]{index=1}
import io.swagger.v3.oas.annotations.parameters.RequestBody;                  // :contentReference[oaicite:2]{index=2}
import io.swagger.v3.oas.annotations.responses.ApiResponse;                  // :contentReference[oaicite:3]{index=3}
import io.swagger.v3.oas.annotations.media.Content;                          // :contentReference[oaicite:4]{index=4}
import io.swagger.v3.oas.annotations.media.Schema;                           // :contentReference[oaicite:5]{index=5}

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication", description = "Login, registration, token, and email-verify endpoints")  // :contentReference[oaicite:6]{index=6}
public class AuthController {

    private final AuthServiceImpl authService;
    private final VerificationTokenService verificationTokenService;
    private final UserRepository userRepository;

    @Operation(
            summary = "User login",
            description = "Authenticate and return access & refresh JWTs",
            requestBody = @RequestBody(
                    description = "User credentials",
                    required    = true,
                    content     = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = AuthRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(
            summary = "User registration",
            description = "Register and return access & refresh JWTs",
            requestBody = @RequestBody(
                    description = "Registration details",
                    required    = true,
                    content     = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = RegisterRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Registration successful",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Email already exists")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(
            summary = "Refresh access token",
            description = "Issue a new access token using a valid refresh token",
            requestBody = @RequestBody(
                    description = "Refresh token",
                    required    = true,
                    content     = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = RefreshTokenRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token refreshed",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
            }
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshAccessToken(request.getRefreshToken()));
    }

    @Operation(
            summary = "Logout user",
            description = "Invalidate the given refresh token",
            requestBody = @RequestBody(
                    description = "Refresh token to invalidate",
                    required    = true,
                    content     = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = RefreshTokenRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Logged out successfully")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verify email",
            description = "Activate account using a verification token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email verified"),
                    @ApiResponse(responseCode = "400", description = "Invalid or expired token")
            }
    )
    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String tokenStr) {
        VerificationToken token = verificationTokenService.findByToken(tokenStr)
                .orElseThrow(() -> new CustomException("Invalid verification token", HttpStatus.BAD_REQUEST));
        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenService.deleteByUser(user);
        return ResponseEntity.ok("Email verified successfully!");
    }
}