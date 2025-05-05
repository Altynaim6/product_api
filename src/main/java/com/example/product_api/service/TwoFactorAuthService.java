package com.example.product_api.service;

public interface TwoFactorAuthService {
    String generateSecretKey();
    boolean verifyCode(String secret, int code);
}
