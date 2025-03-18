package com.example.product_api.service.impl;

import com.example.product_api.exception.CustomException;
import com.example.product_api.mapper.CategoryMapper;
import com.example.product_api.model.domain.Category;
import com.example.product_api.model.dto.category.CategoryRequest;
import com.example.product_api.model.dto.category.CategoryResponse;
import com.example.product_api.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

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
    void add_Success() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Electronics");
        request.setDescription("Category for electronics");

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        CategoryResponse response = new CategoryResponse();
        response.setId(1L);
        response.setName(request.getName());
        response.setDescription(request.getDescription());

        when(categoryRepository.existsByName(request.getName())).thenReturn(false);
        when(categoryMapper.toCategory(any(Category.class), eq(request))).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(response);

        CategoryResponse result = categoryService.add(request);

        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getDescription(), result.getDescription());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void add_CategoryAlreadyExists() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Electronics");

        when(categoryRepository.existsByName(request.getName())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> categoryService.add(request));
        assertEquals("Category is already exists", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void update_Success() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Updated Electronics");
        request.setDescription("Updated description");

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        CategoryResponse response = new CategoryResponse();
        response.setId(1L);
        response.setName(request.getName());
        response.setDescription(request.getDescription());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName(request.getName())).thenReturn(false);
        when(categoryMapper.toCategory(category, request)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(response);

        CategoryResponse result = categoryService.update(request, 1L);

        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getDescription(), result.getDescription());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void update_CategoryNotFound() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Updated Electronics");

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> categoryService.update(request, 1L));
        assertEquals("Category not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void update_CategoryAlreadyExists() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Electronics");

        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName(request.getName())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> categoryService.update(request, 1L));
        assertEquals("Category is already exists", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void getById_Success() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        CategoryResponse response = new CategoryResponse();
        response.setId(1L);
        response.setName("Electronics");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponse(category)).thenReturn(response);

        CategoryResponse result = categoryService.getById(1L);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
    }

    @Test
    void getById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> categoryService.getById(1L));
        assertEquals("Category not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void all_Success() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Electronics");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Clothing");

        List<Category> categories = List.of(category1, category2);
        CategoryResponse response = new CategoryResponse();
        response.setId(1L);
        response.setName("Electronics");
        response.setDescription("Description");

        CategoryResponse response1 = new CategoryResponse();
        response1.setId(1L);
        response1.setName("Clothing");
        response1.setDescription("Description");
        List<CategoryResponse> categoryResponses = List.of(response, response1);

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toResponseList(categories)).thenReturn(categoryResponses);

        List<CategoryResponse> result = categoryService.all();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }
}