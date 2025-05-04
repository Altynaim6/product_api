package com.example.product_api.service.impl;

import com.example.product_api.config.JwtService;
import com.example.product_api.exception.CustomException;
import com.example.product_api.model.domain.User;
import com.example.product_api.model.dto.auth.AuthRequest;
import com.example.product_api.model.dto.auth.AuthResponse;
import com.example.product_api.model.dto.auth.RegisterRequest;
import com.example.product_api.model.enums.Role;
import com.example.product_api.repository.UserRepository;
import com.example.product_api.service.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock AuthenticationManager authenticationManager;
    @Mock
    UserRepository userRepository;
    @Mock JwtService jwtService;
    @Mock PasswordEncoder passwordEncoder;
    @Mock
    RefreshTokenService refreshTokenService;

    @InjectMocks AuthServiceImpl authService;

    private final long refreshDuration = 3_600_000L; // 1h

    @BeforeEach
    void init() {
        // Inject the @Value field
        ReflectionTestUtils.setField(authService, "refreshTokenDurationMs", refreshDuration);
    }

    @Test @DisplayName("login(): valid credentials → returns both tokens")
    void login_validCredentials() {
        AuthRequest req = new AuthRequest();
        req.setEmail("u@e.com");
        req.setPassword("pwd");

        User user = new User();
        user.setEmail(req.getEmail());
        user.setRole(Role.CUSTOMER);

        when(userRepository.findByEmail(req.getEmail()))
                .thenReturn(Optional.of(user));
        Authentication dummyAuth = new UsernamePasswordAuthenticationToken(
                /* principal */ "user@example.com",
                /* credentials */ null,
                /* authorities */ Collections.emptyList()
        );
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(dummyAuth);

        when(jwtService.generateToken(user)).thenReturn("access");
        when(refreshTokenService.createAndSaveRefreshToken(user, refreshDuration))
                .thenReturn("refresh");

        AuthResponse resp = authService.login(req);
        assertEquals("access",  resp.getAccessToken());
        assertEquals("refresh", resp.getRefreshToken());
        verify(authenticationManager).authenticate(any());
    }

    @Test @DisplayName("login(): bad credentials → BadCredentialsException bubbles up")
    void login_badCredentials() {
        AuthRequest req = new AuthRequest();
        req.setEmail("u@e.com");
        req.setPassword("wrong");

        doThrow(new BadCredentialsException("bad"))
                .when(authenticationManager)
                .authenticate(any());

        assertThrows(BadCredentialsException.class, () -> authService.login(req));
    }

    @Test @DisplayName("login(): user not found → CustomException(NOT_FOUND)")
    void login_userNotFound() {
        AuthRequest req = new AuthRequest();
        req.setEmail("nouser@e.com");
        req.setPassword("pwd");

        when(userRepository.findByEmail(req.getEmail()))
                .thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> authService.login(req));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test @DisplayName("register(): new email → returns both tokens")
    void register_newUser() {
        RegisterRequest req = new RegisterRequest();
        req.setName("Alice");
        req.setEmail("a@e.com");
        req.setPassword("secret123");

        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret123")).thenReturn("enc");
        ArgumentCaptor<User> cap = ArgumentCaptor.forClass(User.class);

        User saved = new User();
        saved.setName("Alice");
        saved.setEmail("a@e.com");
        saved.setPassword("enc");
        saved.setRole(Role.CUSTOMER);
        when(userRepository.save(cap.capture())).thenReturn(saved);

        when(jwtService.generateToken(saved)).thenReturn("tokA");
        when(refreshTokenService.createAndSaveRefreshToken(saved, refreshDuration))
                .thenReturn("tokR");

        AuthResponse r = authService.register(req);
        assertEquals("tokA", r.getAccessToken());
        assertEquals("tokR", r.getRefreshToken());

        User created = cap.getValue();
        assertEquals("Alice", created.getName());
        assertEquals(Role.CUSTOMER, created.getRole());
    }

    @Test @DisplayName("register(): email exists → CustomException(CONFLICT)")
    void register_emailExists() {
        RegisterRequest req = new RegisterRequest();
        req.setName("Bob");
        req.setEmail("b@e.com");
        req.setPassword("abc123");

        when(userRepository.findByEmail(req.getEmail()))
                .thenReturn(Optional.of(new User()));

        CustomException ex = assertThrows(CustomException.class, () -> authService.register(req));
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test @DisplayName("refreshAccessToken(): valid token → rotates both tokens")
    void refreshAccessToken_valid() {
        String old = "oldref";
        User user = new User();
        user.setEmail("u@e.com");

        when(refreshTokenService.verifyExpirationAndGetUser(old)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("newA");
        when(refreshTokenService.rotateRefreshToken(old, refreshDuration))
                .thenReturn("newR");

        AuthResponse r = authService.refreshAccessToken(old);
        assertEquals("newA", r.getAccessToken());
        assertEquals("newR", r.getRefreshToken());
    }

    @Test @DisplayName("refreshAccessToken(): expired token → CustomException")
    void refreshAccessToken_expired() {
        when(refreshTokenService.verifyExpirationAndGetUser("bad"))
                .thenThrow(new CustomException("expired", HttpStatus.UNAUTHORIZED));

        CustomException ex = assertThrows(CustomException.class,
                () -> authService.refreshAccessToken("bad"));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
    }

    @Test @DisplayName("logout(): always calls deleteByToken")
    void logout_always() {
        String ref = "todel";
        doNothing().when(refreshTokenService).deleteByToken(ref);

        authService.logout(ref);
        verify(refreshTokenService).deleteByToken(ref);
    }
}