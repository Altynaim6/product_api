package com.example.product_api.service;

import com.example.product_api.exception.CustomException;
import com.example.product_api.model.domain.RefreshToken;
import com.example.product_api.model.domain.User;
import com.example.product_api.repository.RefreshTokenRepository;
import com.example.product_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenDurationMs;

    public String createAndSaveRefreshToken(User user, Long durationMs) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusMillis(durationMs));
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    public User verifyExpirationAndGetUser(String tokenStr) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new CustomException("Invalid refresh token", HttpStatus.UNAUTHORIZED));

        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new CustomException("Refresh token expired", HttpStatus.UNAUTHORIZED);
        }
        return token.getUser();
    }

    public String rotateRefreshToken(String oldToken, Long durationMs) {
        RefreshToken token = refreshTokenRepository.findByToken(oldToken)
                .orElseThrow(() -> new CustomException("Invalid refresh token", HttpStatus.UNAUTHORIZED));

        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusMillis(durationMs));
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    @Transactional
    public void deleteByToken(String tokenStr) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new CustomException("Refresh token not found", HttpStatus.NOT_FOUND));
        refreshTokenRepository.delete(token);
    }
}