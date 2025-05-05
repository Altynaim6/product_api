package com.example.product_api.service;

import com.example.product_api.model.domain.RefreshToken;
import com.example.product_api.model.domain.User;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    Optional<RefreshToken> findByToken(String token);
    RefreshToken verifyExpiration(RefreshToken token);
    RefreshToken getOrCreateRefreshToken(User user);
    void deleteByUserId(Long userId);
}
