package com.example.product_api.repository;

import com.example.product_api.model.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        // Prepare test data
        category = new Category();
        category.setName("Electronics");
        category.setDescription("Devices and gadgets");
        categoryRepository.save(category);
    }

    @Test
    void existsByName() {
        // Test if the category exists by its name
        assertTrue(categoryRepository.existsByName("Electronics"));
        assertFalse(categoryRepository.existsByName("NonExistentCategory"));
    }

    @Test
    void findByName() {
        // Test finding a category by its name
        Optional<Category> foundCategory = categoryRepository.findByName("Electronics");
        assertTrue(foundCategory.isPresent());
        assertEquals("Electronics", foundCategory.get().getName());
        assertEquals("Devices and gadgets", foundCategory.get().getDescription());

        // Test finding a category that does not exist
        Optional<Category> nonExistentCategory = categoryRepository.findByName("NonExistentCategory");
        assertFalse(nonExistentCategory.isPresent());
    }
}