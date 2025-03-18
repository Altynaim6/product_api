package com.example.product_api.service.impl;

import com.example.product_api.exception.CustomException;
import com.example.product_api.mapper.UserMapper;
import com.example.product_api.model.domain.User;
import com.example.product_api.model.dto.user.UserRequest;
import com.example.product_api.model.dto.user.UserResponse;
import com.example.product_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");

        userRequest = new UserRequest();
        userRequest.setName("John Doe");
        userRequest.setEmail("johndoe@example.com");
        userRequest.setPassword("password123");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("John Doe");
        userResponse.setEmail("johndoe@example.com");
    }

    @Test
    void all_ShouldReturnUserList() {
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(userPage);
        when(userMapper.toResponseList(userPage.getContent())).thenReturn(List.of(userResponse));

        List<UserResponse> result = userService.all(0, 10);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(userRepository).findAll(PageRequest.of(0, 10));
        verify(userMapper).toResponseList(userPage.getContent());
    }

    @Test
    void findById_ShouldReturnUserResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.findById(1L);

        assertEquals("John Doe", result.getName());
        assertEquals("johndoe@example.com", result.getEmail());
        verify(userRepository).findById(1L);
        verify(userMapper).toResponse(user);
    }

    @Test
    void findById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> userService.findById(1L));

        assertEquals("User Not Found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(userRepository).findById(1L);
    }

    @Test
    void update_ShouldReturnUpdatedUserResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toUser(user, userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.update(1L, userRequest);

        assertEquals("John Doe", result.getName());
        verify(userRepository).findById(1L);
        verify(userRepository).findByEmail(userRequest.getEmail());
        verify(userRepository).save(user);
        verify(userMapper).toResponse(user);
    }

    @Test
    void update_ShouldThrowException_WhenEmailAlreadyExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(new User()));

        CustomException exception = assertThrows(CustomException.class, () -> userService.update(1L, userRequest));

        assertEquals("User already exists", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        verify(userRepository).findById(1L);
        verify(userRepository).findByEmail(userRequest.getEmail());
    }

    @Test
    void save_ShouldReturnUserResponse() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toUser(any(User.class), eq(userRequest))).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.save(userRequest);

        assertEquals("John Doe", result.getName());
        verify(userRepository).findByEmail(userRequest.getEmail());
        verify(userRepository).save(user);
        verify(userMapper).toResponse(user);
    }

    @Test
    void save_ShouldThrowException_WhenEmailAlreadyExists() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(user));

        CustomException exception = assertThrows(CustomException.class, () -> userService.save(userRequest));

        assertEquals("User already exists", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        verify(userRepository).findByEmail(userRequest.getEmail());
    }

    @Test
    void deleteById_ShouldDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteById(1L));

        verify(userRepository).deleteById(1L);
    }
}