package com.example.product_api.controller;

import com.example.product_api.exception.CustomException;
import com.example.product_api.model.domain.User;
import com.example.product_api.model.dto.auth.AuthRequest;
import com.example.product_api.model.dto.auth.AuthResponse;
import com.example.product_api.model.dto.user.UserRequest;
import com.example.product_api.service.EmailSenderService;
import com.example.product_api.service.RefreshTokenService;
import com.example.product_api.service.VerificationTokenService;
import com.example.product_api.security.jwt.JwtTokenProvider;
import com.example.product_api.repository.UserRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private VerificationTokenService verificationTokenService;
    @Mock
    private EmailSenderService emailSenderService;

    @InjectMocks
    private AuthController authController;

    private UserRequest userRequest;
    private AuthRequest authRequest;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userRequest = new UserRequest();
        userRequest.setName("John Doe");
        userRequest.setEmail("john.doe@example.com");
        userRequest.setPassword("password");

        authRequest = new AuthRequest();
        authRequest.setEmail("john.doe@example.com");
        authRequest.setPassword("password");

        user = new User();
        user.setEmail("john.doe@example.com");
        user.setName("John Doe");
        user.setPassword("encodedPassword");
        user.setEnabled(false);
    }

    @Test
    void register_shouldReturnSuccessMessage() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(java.util.Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(verificationTokenService.createToken(user)).thenReturn(mock(com.example.product_api.model.domain.VerificationToken.class));
        doNothing().when(emailSenderService).sendVerificationEmail(anyString(), anyString());

        ResponseEntity<String> response = authController.register(userRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registration successful. Please check your email to verify your account.", response.getBody());
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyRegistered() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(java.util.Optional.of(user));

        CustomException exception = assertThrows(CustomException.class, () -> authController.register(userRequest));
        assertEquals("Email is already registered", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }


    @Test
    void login_shouldThrowException_whenInvalidCredentials() {
        when(authenticationManager.authenticate(any())).thenThrow(new org.springframework.security.authentication.BadCredentialsException("Invalid credentials"));

        CustomException exception = assertThrows(CustomException.class, () -> authController.login(authRequest));
        assertEquals("Invalid credentials", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }


    @Test
    void refreshToken_shouldThrowException_whenRefreshTokenNotFound() {
        when(refreshTokenService.findByToken(anyString())).thenReturn(java.util.Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> authController.refreshToken(java.util.Map.of("refresh_token", "invalidRefreshToken")));
        assertEquals("Refresh token not found or expired", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void verifyAccount_shouldReturnRedirect_whenTokenIsValid() {
        String token = "validToken";
        com.example.product_api.model.domain.VerificationToken verificationToken = mock(com.example.product_api.model.domain.VerificationToken.class);
        when(verificationTokenService.findByToken(token)).thenReturn(java.util.Optional.of(verificationToken));
        when(verificationToken.getExpiryDate()).thenReturn(Instant.now().plusSeconds(3600));
        when(verificationToken.getUser()).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        ResponseEntity<Void> response = authController.verifyAccount(token);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals("/login.html", response.getHeaders().getLocation().getPath());
    }

    @Test
    void verifyAccount_shouldThrowException_whenTokenExpired() {
        String token = "expiredToken";
        com.example.product_api.model.domain.VerificationToken verificationToken = mock(com.example.product_api.model.domain.VerificationToken.class);
        when(verificationTokenService.findByToken(token)).thenReturn(java.util.Optional.of(verificationToken));
        when(verificationToken.getExpiryDate()).thenReturn(Instant.now().minusSeconds(3600));

        CustomException exception = assertThrows(CustomException.class, () -> authController.verifyAccount(token));
        assertEquals("Verification token has expired", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}