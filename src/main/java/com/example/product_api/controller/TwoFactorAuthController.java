package com.example.product_api.controller;

import com.example.product_api.exception.CustomException;
import com.example.product_api.model.domain.User;
import com.example.product_api.repository.UserRepository;
import com.example.product_api.service.TwoFactorAuthService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/2fa")
@RequiredArgsConstructor
public class TwoFactorAuthController {

    private final TwoFactorAuthService twoFactorAuthService;
    private final UserRepository userRepository;

    @PostMapping("/enable")
    public ResponseEntity<String> enable2FA(@RequestParam @Email String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        String secretKey = twoFactorAuthService.generateSecretKey();
        user.setTwoFaSecret(secretKey);
        userRepository.save(user);

        return ResponseEntity.ok("2FA enabled. Secret key: " + secretKey +
                ". Add it in Google Authenticator manually.");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify2FA(
            @RequestParam @Email String email,
            @RequestParam int code
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        boolean isValid = twoFactorAuthService.verifyCode(user.getTwoFaSecret(), code);

        if (isValid) {
            return ResponseEntity.ok("✅ 2FA verification successful");
        } else {
            throw new CustomException("❌ Invalid 2FA code", HttpStatus.UNAUTHORIZED);
        }
    }
}
