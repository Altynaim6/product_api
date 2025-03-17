package com.example.product_api.mapper;

import com.example.product_api.model.domain.User;
import com.example.product_api.model.dto.user.UserRequest;
import com.example.product_api.model.dto.user.UserResponse;
import com.example.product_api.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUserRequestToUserEntity() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Alice");
        userRequest.setEmail("alice@example.com");
        userRequest.setPassword("password123");
        userRequest.setRole("SELLER");

        User user = userMapper.toUser(new User(), userRequest);

        assertNotNull(user);
        assertEquals("Alice", user.getName());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(Role.SELLER, user.getRole());
    }

    @Test
    public void testUserEntityToUserResponse() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setRole(Role.SELLER);

        // Act
        UserResponse userResponse = userMapper.toResponse(user);

        // Assert
        assertNotNull(userResponse);
        assertEquals(1L, userResponse.getId());
        assertEquals("Alice", userResponse.getName());
        assertEquals("alice@example.com", userResponse.getEmail());
        assertEquals("SELLER", userResponse.getRole()); // Role should be converted to String
    }
}