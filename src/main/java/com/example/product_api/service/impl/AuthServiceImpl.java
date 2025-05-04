    package com.example.product_api.service.impl;

    import com.example.product_api.config.JwtService;
    import com.example.product_api.exception.CustomException;
    import com.example.product_api.model.domain.User;
    import com.example.product_api.model.dto.auth.AuthRequest;
    import com.example.product_api.model.dto.auth.AuthResponse;
    import com.example.product_api.model.dto.auth.RegisterRequest;
    import com.example.product_api.model.enums.Role;
    import com.example.product_api.repository.UserRepository;
    import com.example.product_api.service.AuthService;
    import com.example.product_api.service.RefreshTokenService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class AuthServiceImpl implements AuthService {
        private final AuthenticationManager authenticationManager;
        private final UserRepository userRepository;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;
        private final RefreshTokenService refreshTokenService;

        @Value("${jwt.refresh-token.expiration}")
        private Long refreshTokenDurationMs;

        @Override
        public AuthResponse login(AuthRequest request) {
            // 1) Authenticate credentials
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()
                    )
            );
            // 2) Load user
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
            // 3) Generate tokens
            String accessToken = jwtService.generateToken(user);
            String refreshToken = refreshTokenService.createAndSaveRefreshToken(user, refreshTokenDurationMs);
            // 4) Return both tokens
            return new AuthResponse(accessToken, refreshToken);
        }



        public AuthResponse refreshAccessToken(String refreshToken) {
            User user = refreshTokenService.verifyExpirationAndGetUser(refreshToken);
            String newAccessToken = jwtService.generateToken(user);
            String newRefreshToken = refreshTokenService.rotateRefreshToken(refreshToken, refreshTokenDurationMs);
            return new AuthResponse(newAccessToken, newRefreshToken);
        }

        public void logout(String refreshToken) {
            refreshTokenService.deleteByToken(refreshToken);
        }
    }