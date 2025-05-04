package com.example.product_api.config;

import com.example.product_api.exception.CustomException;
import com.example.product_api.model.domain.User;
import com.example.product_api.model.enums.Role;
import com.example.product_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationConfig applicationConfig;

    @BeforeEach
    void setUp() {
        // no special setup required
    }

    @Test
    void authenticationProvider_shouldAuthenticateValidUser() {
        // Arrange
        String email = "user@example.com";
        String rawPassword = "password123";
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);

        User userEntity = new User();
        userEntity.setEmail(email);
        userEntity.setPassword(encodedPassword);
        userEntity.setRole(Role.CUSTOMER);
        userEntity.setEnabled(true);      // <— mark the user as enabled

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // Act
        AuthenticationProvider provider = applicationConfig.authenticationProvider();
        Authentication authToken = new UsernamePasswordAuthenticationToken(email, rawPassword);
        Authentication result = provider.authenticate(authToken);

        // Assert
        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        assertEquals(email, result.getName());
    }

    @Test
    void authenticationManager_shouldDelegateToConfiguration() throws Exception {
        // Arrange
        AuthenticationConfiguration authConfig = mock(AuthenticationConfiguration.class);
        var mockManager = mock(AuthenticationManager.class);
        when(authConfig.getAuthenticationManager()).thenReturn(mockManager);

        // Act
        var result = applicationConfig.authenticationManager(authConfig);

        // Assert
        assertSame(mockManager, result);
        verify(authConfig).getAuthenticationManager();
    }
}