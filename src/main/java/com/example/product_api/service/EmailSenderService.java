package com.example.product_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailSenderService {

    public void sendVerificationEmail(String to, String link) {
        log.info("Отправка верификационной ссылки на email: {}", to);
        log.info("Ссылка для верификации: {}", link);
    }
}