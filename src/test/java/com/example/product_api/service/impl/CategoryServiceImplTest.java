package com.example.product_api.service.impl;

import com.example.product_api.exception.CustomException;
import com.example.product_api.mapper.CategoryMapper;
import com.example.product_api.model.domain.Category;
import com.example.product_api.model.dto.category.CategoryRequest;
import com.example.product_api.model.dto.category.CategoryResponse;
import com.example.product_api.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void add_shouldAddCategorySuccessfully() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Electronics");

        Category category = new Category();
        CategoryResponse response = new CategoryResponse();

        when(categoryRepository.existsByName("Electronics")).thenReturn(false);
        when(categoryMapper.toCategory(any(Category.class), eq(request))).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(response);

        CategoryResponse result = categoryService.add(request);

        assertEquals(response, result);
        verify(categoryRepository).save(category);
    }

    @Test
    void add_shouldThrowExceptionWhenCategoryExists() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Electronics");

        when(categoryRepository.existsByName("Electronics")).thenReturn(true);

        CustomException ex = assertThrows(CustomException.class, () -> categoryService.add(request));
        assertEquals("Category is already exists", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    void update_shouldUpdateCategorySuccessfully() {
        Long id = 1L;
        CategoryRequest request = new CategoryRequest();
        request.setName("Home");

        Category existing = new Category();
        Category updated = new Category();
        CategoryResponse response = new CategoryResponse();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.existsByName("Home")).thenReturn(false);
        when(categoryMapper.toCategory(existing, request)).thenReturn(updated);
        when(categoryRepository.save(updated)).thenReturn(updated);
        when(categoryMapper.toResponse(updated)).thenReturn(response);

        CategoryResponse result = categoryService.update(request, id);

        assertEquals(response, result);
    }

    @Test
    void update_shouldThrowExceptionWhenNotFound() {
        Long id = 99L;
        CategoryRequest request = new CategoryRequest();

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> categoryService.update(request, id));
        assertEquals("Category not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void update_shouldThrowExceptionWhenNameExists() {
        Long id = 1L;
        CategoryRequest request = new CategoryRequest();
        request.setName("Books");

        Category existing = new Category();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.existsByName("Books")).thenReturn(true);

        CustomException ex = assertThrows(CustomException.class, () -> categoryService.update(request, id));
        assertEquals("Category is already exists", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    void getById_shouldReturnCategoryResponse() {
        Long id = 1L;
        Category category = new Category();
        CategoryResponse response = new CategoryResponse();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponse(category)).thenReturn(response);

        CategoryResponse result = categoryService.getById(id);

        assertEquals(response, result);
    }

    @Test
    void getById_shouldThrowExceptionWhenNotFound() {
        Long id = 999L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> categoryService.getById(id));
        assertEquals("Category not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void all_shouldReturnListOfCategories() {
        List<Category> categories = List.of(new Category(), new Category());
        List<CategoryResponse> responses = List.of(new CategoryResponse(), new CategoryResponse());

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toResponseList(categories)).thenReturn(responses);

        List<CategoryResponse> result = categoryService.all();

        assertEquals(responses.size(), result.size());
    }
}
