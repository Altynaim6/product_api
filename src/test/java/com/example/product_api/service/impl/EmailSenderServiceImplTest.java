package com.example.product_api.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailSenderServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailSenderServiceImpl emailSenderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendVerificationEmail_shouldSendEmailSuccessfully() throws MessagingException {
        // given
        String to = "test@example.com";
        String link = "http://example.com/verify";

        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailSenderService.sendVerificationEmail(to, link);

        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendVerificationEmail_shouldThrowRuntimeExceptionOnError() {
        // given
        String to = "error@example.com";
        String link = "http://example.com/verify";
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Simulated failure")).when(mailSender).send(any(MimeMessage.class));

        // when & then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            emailSenderService.sendVerificationEmail(to, link);
        });

        assertEquals("Failed to send email", ex.getMessage());
    }
}