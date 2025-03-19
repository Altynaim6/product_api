package com.example.product_api.repository;

import com.example.product_api.model.domain.User;
import com.example.product_api.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Create a test user
        testUser = new User();
        testUser.setName("John Doe");
        testUser.setEmail("johndoe@example.com");
        testUser.setPassword("securepassword");
        testUser.setRole(Role.SELLER);

        // Save the user in the database
        userRepository.save(testUser);
    }

    @Test
    void findByEmail_shouldReturnUser_whenEmailExists() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("johndoe@example.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
        assertEquals("johndoe@example.com", foundUser.get().getEmail());
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailDoesNotExist() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertFalse(foundUser.isPresent());
    }
}