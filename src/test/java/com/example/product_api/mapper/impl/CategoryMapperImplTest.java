package com.example.product_api.mapper.impl;

import com.example.product_api.model.domain.Category;
import com.example.product_api.model.dto.category.CategoryRequest;
import com.example.product_api.model.dto.category.CategoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperImplTest {

    private CategoryMapperImpl categoryMapper;

    @BeforeEach
    void setUp() {
        categoryMapper = new CategoryMapperImpl();
    }

    @Test
    void toCategory() {
        Category category = new Category();
        CategoryRequest request = new CategoryRequest();
        request.setName("Electronics");
        request.setDescription("Devices and gadgets");

        Category mappedCategory = categoryMapper.toCategory(category, request);

        assertNotNull(mappedCategory);
        assertEquals("Electronics", mappedCategory.getName());
        assertEquals("Devices and gadgets", mappedCategory.getDescription());
    }

    @Test
    void toResponse() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Books");
        category.setDescription("All kinds of books");

        CategoryResponse response = categoryMapper.toResponse(category);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Books", response.getName());
        assertEquals("All kinds of books", response.getDescription());
    }

    @Test
    void toResponseList() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Fashion");
        category1.setDescription("Clothing and accessories");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Sports");
        category2.setDescription("Sports equipment and accessories");

        List<CategoryResponse> responses = categoryMapper.toResponseList(List.of(category1, category2));

        assertNotNull(responses);
        assertEquals(2, responses.size());

        assertEquals(1L, responses.get(0).getId());
        assertEquals("Fashion", responses.get(0).getName());
        assertEquals("Clothing and accessories", responses.get(0).getDescription());

        assertEquals(2L, responses.get(1).getId());
        assertEquals("Sports", responses.get(1).getName());
        assertEquals("Sports equipment and accessories", responses.get(1).getDescription());
    }
}