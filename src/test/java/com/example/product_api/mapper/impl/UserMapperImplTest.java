package com.example.product_api.mapper.impl;

import com.example.product_api.model.domain.User;
import com.example.product_api.model.dto.user.UserRequest;
import com.example.product_api.model.dto.user.UserResponse;
import com.example.product_api.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperImplTest {

    private UserMapperImpl userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void toUser() {
        User user = new User();
        UserRequest request = new UserRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("securepassword");
        request.setRole("ADMIN");

        User mappedUser = userMapper.toUser(user, request);

        assertNotNull(mappedUser);
        assertEquals("John Doe", mappedUser.getName());
        assertEquals("john.doe@example.com", mappedUser.getEmail());
        assertEquals("securepassword", mappedUser.getPassword());
        assertEquals(Role.SELLER, mappedUser.getRole());
    }

    @Test
    void toUser_InvalidRole_ThrowsException() {
        User user = new User();
        UserRequest request = new UserRequest();
        request.setName("Jane Doe");
        request.setEmail("jane.doe@example.com");
        request.setPassword("anotherpassword");
        request.setRole("INVALID_ROLE");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userMapper.toUser(user, request);
        });

        assertTrue(exception.getMessage().contains("Invalid role"));
    }

    @Test
    void toResponse() {
        User user = new User();
        user.setId(1L);
        user.setName("Alice Smith");
        user.setEmail("alice.smith@example.com");
        user.setRole(Role.CUSTOMER);

        UserResponse response = userMapper.toResponse(user);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Alice Smith", response.getName());
        assertEquals("alice.smith@example.com", response.getEmail());
        assertEquals("USER", response.getRole());
    }

    @Test
    void toResponse_NullUser_ReturnsNull() {
        assertNull(userMapper.toResponse(null));
    }

    @Test
    void toResponseList() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");
        user1.setEmail("alice@example.com");
        user1.setRole(Role.CUSTOMER);

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");
        user2.setEmail("bob@example.com");
        user2.setRole(Role.SELLER);

        List<UserResponse> responses = userMapper.toResponseList(List.of(user1, user2));

        assertNotNull(responses);
        assertEquals(2, responses.size());

        assertEquals(1L, responses.get(0).getId());
        assertEquals("Alice", responses.get(0).getName());
        assertEquals("alice@example.com", responses.get(0).getEmail());
        assertEquals("USER", responses.get(0).getRole());

        assertEquals(2L, responses.get(1).getId());
        assertEquals("Bob", responses.get(1).getName());
        assertEquals("bob@example.com", responses.get(1).getEmail());
        assertEquals("ADMIN", responses.get(1).getRole());
    }

    @Test
    void toResponseList_EmptyList_ReturnsEmptyList() {
        List<UserResponse> responses = userMapper.toResponseList(List.of());

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void toResponseList_NullList_ReturnsEmptyList() {
        List<UserResponse> responses = userMapper.toResponseList(null);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }
}