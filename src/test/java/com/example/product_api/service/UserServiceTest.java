package com.example.product_api.service;

import com.example.product_api.model.domain.User;
import com.example.product_api.model.dto.user.UserRequest;
import com.example.product_api.model.dto.user.UserResponse;
import com.example.product_api.model.enums.Role;
import com.example.product_api.repository.UserRepository;
import com.example.product_api.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("password123");
        user.setRole(Role.CUSTOMER);

        userRequest = new UserRequest();
        userRequest.setName("Alice");
        userRequest.setEmail("alice@example.com");
        userRequest.setPassword("password123");
        userRequest.setRole("SELLER");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("Alice");
        userResponse.setEmail("alice@example.com");
        userResponse.setRole(String.valueOf(Role.CUSTOMER));
    }

    @Test
    public void testCreateUser() {
        when(userMapper.toUser(new User(), userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse createdUser = userService.save(userRequest);

        assertNotNull(createdUser);
        assertEquals("Alice", createdUser.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals("Alice", foundUser.getName());
        verify(userRepository, times(1)).findById(1L);
    }
}