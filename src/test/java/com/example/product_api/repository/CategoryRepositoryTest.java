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
        category = new Category();
        category.setName("Electronics");
        category.setDescription("Electronic devices and gadgets");
        categoryRepository.save(category);
    }

    @Test
    void existsByName_ShouldReturnTrue_WhenCategoryExists() {
        boolean exists = categoryRepository.existsByName("Electronics");
        assertTrue(exists, "Category with name 'Electronics' should exist");
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenCategoryDoesNotExist() {
        boolean exists = categoryRepository.existsByName("NonExistingCategory");
        assertFalse(exists, "Category with name 'NonExistingCategory' should not exist");
    }

    @Test
    void findByName_ShouldReturnCategory_WhenNameExists() {
        Optional<Category> foundCategory = categoryRepository.findByName("Electronics");
        assertTrue(foundCategory.isPresent(), "Category should be found");
        assertEquals("Electronics", foundCategory.get().getName());
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenNameDoesNotExist() {
        Optional<Category> foundCategory = categoryRepository.findByName("NonExistingCategory");
        assertFalse(foundCategory.isPresent(), "No category should be found");
    }
}