package com.example.product_api.controller;

import com.example.product_api.model.domain.User;
import com.example.product_api.repository.UserRepository;
import com.example.product_api.service.TwoFactorAuthService;
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

    @PostMapping("/verify")
    public ResponseEntity<String> verify2FA(@RequestParam String email, @RequestParam int code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean valid = twoFactorAuthService.verifyCode(user.getSecret2FA(), code);

        if (valid) {
            return ResponseEntity.ok("✅ 2FA verified successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Invalid 2FA code");
        }
    }
}
