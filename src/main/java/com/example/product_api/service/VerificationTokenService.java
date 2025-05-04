package com.example.product_api.service;

import com.example.product_api.model.domain.User;
import com.example.product_api.model.domain.VerificationToken;
import com.example.product_api.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationToken createToken(User user) {
        VerificationToken token = new VerificationToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusSeconds(3600)); // 1 час
        return verificationTokenRepository.save(token);
    }

    public Optional<VerificationToken> findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public void deleteByUser(User user) {
        verificationTokenRepository.deleteByUser(user);
    }
}