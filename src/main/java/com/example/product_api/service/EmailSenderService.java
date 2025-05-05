package com.example.product_api.service;

public interface EmailSenderService {
    void sendVerificationEmail(String to, String link);
}
