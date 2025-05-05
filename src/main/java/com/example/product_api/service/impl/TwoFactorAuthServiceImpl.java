package com.example.product_api.service.impl;

import com.example.product_api.service.TwoFactorAuthService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorAuthServiceImpl implements TwoFactorAuthService {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    @Override
    public String generateSecretKey() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    @Override
    public boolean verifyCode(String secret, int code) {
        return gAuth.authorize(secret, code);
    }
}
