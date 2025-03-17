package com.example.product_api.controller;

import com.example.product_api.model.dto.category.CategoryRequest;
import com.example.product_api.model.dto.category.CategoryResponse;
import com.example.product_api.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testAddCategory() throws Exception {
        // Arrange
        CategoryRequest request = new CategoryRequest();
        request.setName("Electronics");

        CategoryResponse response = new CategoryResponse();
        response.setId(1L);
        response.setName("Electronics");

        when(categoryService.add(any(CategoryRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    public void testUpdateCategory() throws Exception {
        // Arrange
        CategoryRequest request = new CategoryRequest();
        request.setName("Updated Electronics");

        CategoryResponse response = new CategoryResponse();
        response.setId(1L);
        response.setName("Updated Electronics");

        when(categoryService.update(any(CategoryRequest.class), any(Long.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Electronics"));
    }

    @Test
    public void testGetCategoryById() throws Exception {
        // Arrange
        CategoryResponse response = new CategoryResponse();
        response.setId(1L);
        response.setName("Electronics");

        when(categoryService.getById(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    public void testGetAllCategories() throws Exception {
        // Arrange
        CategoryResponse response1 = new CategoryResponse();
        response1.setId(1L);
        response1.setName("Electronics");

        CategoryResponse response2 = new CategoryResponse();
        response2.setId(2L);
        response2.setName("Clothing");

        List<CategoryResponse> categoryList = Arrays.asList(response1, response2);

        when(categoryService.all()).thenReturn(categoryList);

        // Act & Assert
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Clothing"));
    }
}