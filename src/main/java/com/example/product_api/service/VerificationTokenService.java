package com.example.product_api.service;

import com.example.product_api.model.domain.User;
import com.example.product_api.model.domain.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {
    VerificationToken createToken(User user);
    Optional<VerificationToken> findByToken(String token);
    void deleteByUser(User user);
}
