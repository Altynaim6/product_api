package com.example.product_api.config;

import com.example.product_api.exception.CustomException;
import com.example.product_api.model.domain.User;
import com.example.product_api.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtService jwtService;

    private final String secretKey = "Zm9vYmFyMTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3A="; // base64 encoded random
    private final long expirationMs = 1000L * 60 * 60; // 1 hour

    @BeforeEach
    void setUp() {
        // Inject private fields via reflection
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "expiration", expirationMs);
    }

    @Test
    void generateToken_andValidateClaims() {
        // Given
        String username = "user@example.com";
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // When
        String token = jwtService.generateToken(userDetails);

        // Then
        assertNotNull(token);
        Claims claims = jwtService.getClaimsFromToken(token);
        assertEquals(username, claims.getSubject());
        assertNotNull(claims.get("role"));
        // validate expiration
        Date issued = claims.getIssuedAt();
        Date expiry = claims.getExpiration();
        assertTrue(expiry.after(issued));
        assertTrue(expiry.getTime() - issued.getTime() == expirationMs);
    }

    @Test
    void getUserEmailFromToken_ShouldReturnUsername() {
        // Given
        String username = "alice@example.com";
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                "pass",
                Collections.emptyList()
        );
        String token = jwtService.generateToken(userDetails);

        // When
        String extracted = jwtService.getUserEmailFromToken(token);

        // Then
        assertEquals(username, extracted);
    }

    @Test
    void getUserFromToken_ShouldReturnUserEntity_WhenExists() {
        // Given
        String username = "bob@example.com";
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                "pass",
                Collections.emptyList()
        );
        String token = jwtService.generateToken(userDetails);

        User mockUser = new User();
        mockUser.setEmail(username);
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(mockUser));

        // When
        User result = jwtService.getUserFromToken("Bearer " + token);

        // Then
        assertNotNull(result);
        assertEquals(username, result.getEmail());
    }

    @Test
    void getUserFromToken_ShouldThrow_WhenUserNotFound() {
        // Given
        String username = "charlie@example.com";
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                "pass",
                Collections.emptyList()
        );
        String token = jwtService.generateToken(userDetails);
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // When & Then
        CustomException ex = assertThrows(CustomException.class,
                () -> jwtService.getUserFromToken("Bearer " + token)
        );
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }
}