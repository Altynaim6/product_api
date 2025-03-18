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

    private User user;

    @BeforeEach
    void setUp() {
        // Setting up a test user and saving it to the repository before each test
        user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);  // Save user to the repository
    }

    @Test
    void findByEmail() {
        // Test for finding user by email
        String email = "john.doe@example.com";
        Optional<User> foundUser = userRepository.findByEmail(email);

        // Verify that the user is found and the email matches
        assertTrue(foundUser.isPresent(), "User should be found by email.");
        assertEquals(email, foundUser.get().getEmail(), "The email should match the expected value.");
        assertEquals("John Doe", foundUser.get().getName(), "The name should match the expected value.");
    }

    @Test
    void findByEmailNotFound() {
        // Test for an email that does not exist in the database
        String nonExistentEmail = "nonexistent.email@example.com";
        Optional<User> foundUser = userRepository.findByEmail(nonExistentEmail);

        // Verify that no user is found for the given email
        assertFalse(foundUser.isPresent(), "User should not be found for a non-existent email.");
    }
}