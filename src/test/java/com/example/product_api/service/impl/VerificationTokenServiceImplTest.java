package com.example.product_api.service.impl;

import com.example.product_api.model.domain.User;
import com.example.product_api.model.domain.VerificationToken;
import com.example.product_api.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerificationTokenServiceImplTest {

    private VerificationTokenServiceImpl tokenService;
    private VerificationTokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        tokenRepository = mock(VerificationTokenRepository.class);
        tokenService = new VerificationTokenServiceImpl(tokenRepository);
    }

    @Test
    void createToken() {
        User user = new User();
        user.setId(1L);

        // Prepare token mock data
        String tokenString = UUID.randomUUID().toString();
        VerificationToken token = new VerificationToken();
        token.setUser(user);
        token.setToken(tokenString);
        token.setExpiryDate(Instant.now().plusSeconds(3600));

        when(tokenRepository.save(any(VerificationToken.class))).thenReturn(token);

        VerificationToken createdToken = tokenService.createToken(user);

        assertNotNull(createdToken);
        assertEquals(user, createdToken.getUser());
        assertEquals(tokenString, createdToken.getToken());
        assertNotNull(createdToken.getExpiryDate());
        verify(tokenRepository, times(1)).save(any(VerificationToken.class));
    }

    @Test
    void findByToken() {
        String token = "some-token";

        VerificationToken mockToken = new VerificationToken();
        mockToken.setToken(token);
        mockToken.setExpiryDate(Instant.now().plusSeconds(3600));

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(mockToken));

        Optional<VerificationToken> foundToken = tokenService.findByToken(token);

        assertTrue(foundToken.isPresent());
        assertEquals(token, foundToken.get().getToken());
        verify(tokenRepository, times(1)).findByToken(token);
    }

    @Test
    void deleteByUser() {
        User user = new User();
        user.setId(1L);

        tokenService.deleteByUser(user);

        verify(tokenRepository, times(1)).deleteByUser(user);
    }
}
