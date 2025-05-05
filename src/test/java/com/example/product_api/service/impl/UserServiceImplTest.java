package com.example.product_api.service.impl;

import com.example.product_api.exception.CustomException;
import com.example.product_api.mapper.UserMapper;
import com.example.product_api.model.domain.User;
import com.example.product_api.model.dto.user.UserRequest;
import com.example.product_api.model.dto.user.UserResponse;
import com.example.product_api.repository.UserRepository;
import com.example.product_api.service.TwoFactorAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TwoFactorAuthService twoFactorAuthService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock data
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setSecret2FA("secret");

        userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");

        userResponse = new UserResponse();
        userResponse.setEmail("test@example.com");
    }


    @Test
    void findById() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // Act
        UserResponse result = userService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void findById_userNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> userService.findById(1L));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void update() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userMapper.toUser(user, userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // Act
        UserResponse result = userService.update(1L, userRequest);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void update_emailAlreadyTaken() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.of(new User()));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> userService.update(1L, userRequest));
        assertEquals("Email already taken by another user", exception.getMessage());
    }

    @Test
    void save() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userMapper.toUser(any(), eq(userRequest))).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // Act
        UserResponse result = userService.save(userRequest);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void save_userExists() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> userService.save(userRequest));
        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    void deleteById() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act
        userService.deleteById(1L);

        // Assert
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteById_userNotFound() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> userService.deleteById(1L));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void enable2FA() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        userService.enable2FA("test@example.com", "secret");

        // Assert
        verify(userRepository).save(user);
    }

    @Test
    void verify2FACode() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(twoFactorAuthService.verifyCode("secret", 123456)).thenReturn(true);

        // Act
        boolean result = userService.verify2FACode("test@example.com", 123456);

        // Assert
        assertTrue(result);
        verify(twoFactorAuthService).verifyCode("secret", 123456);
    }

    @Test
    void verify2FACode_invalidCode() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(twoFactorAuthService.verifyCode("secret", 654321)).thenReturn(false);

        boolean result = userService.verify2FACode("test@example.com", 654321);

        assertFalse(result);
    }
}
