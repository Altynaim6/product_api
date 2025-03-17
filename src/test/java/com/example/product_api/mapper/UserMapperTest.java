package com.example.product_api.mapper;

import com.example.product_api.model.domain.User;
import com.example.product_api.model.dto.user.UserResponse;
import com.example.product_api.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testUserToUserResponse() {
        User user = new User();
        user.setId(1L);
        user.setName("Alina");
        user.setEmail("alina@gmail.com");
        user.setRole(Role.CUSTOMER);

        UserResponse userResponse = userMapper.toResponse(user);

        assertNotNull(userResponse);
        assertEquals(1L, userResponse.getId());
        assertEquals("Alina", userResponse.getName());
        assertEquals("alina@gmail.com", userResponse.getEmail());
        assertEquals("CUSTOMER", userResponse.getRole());
    }
}