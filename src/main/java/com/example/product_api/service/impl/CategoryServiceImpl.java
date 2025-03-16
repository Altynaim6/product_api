package com.example.product_api.service.impl;


import com.example.product_api.exception.CustomException;
import com.example.product_api.mapper.CategoryMapper;
import com.example.product_api.model.domain.Category;
import com.example.product_api.model.dto.category.CategoryRequest;
import com.example.product_api.model.dto.category.CategoryResponse;
import com.example.product_api.repository.CategoryRepository;
import com.example.product_api.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse add(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new CustomException("Category is already exists", HttpStatus.BAD_REQUEST);
        }
        return categoryMapper.toResponse(categoryRepository.save(categoryMapper.toCategory(new Category(), request)));
    }

    @Override
    public CategoryResponse update(CategoryRequest request, Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CustomException("Category not found", HttpStatus.NOT_FOUND));

        if (categoryRepository.existsByName(request.getName())) {
            throw new CustomException("Category is already exists", HttpStatus.BAD_REQUEST);
        }

        return categoryMapper.toResponse(categoryRepository.save(categoryMapper.toCategory(category, request)));
    }

    @Override
    public CategoryResponse getById(Long id) {
        return categoryMapper.toResponse(categoryRepository.findById(id).orElseThrow(() -> new CustomException("Category not found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public List<CategoryResponse> all() {
        return categoryMapper.toResponseList(categoryRepository.findAll());
    }
}