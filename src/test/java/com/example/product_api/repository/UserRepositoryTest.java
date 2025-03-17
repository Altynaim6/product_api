package com.example.product_api.repository;

import com.example.product_api.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindUser() {
        User user = new User();
        user.setName("Altynai");
        user.setEmail("altynai@gmail.com");
        user = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("Altynai", foundUser.get().getName());
        assertEquals("altynai@gmail.com", foundUser.get().getEmail());
    }
}