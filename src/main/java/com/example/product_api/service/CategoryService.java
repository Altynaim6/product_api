package com.example.product_api.service;

import com.example.product_api.model.domain.Category;
import com.example.product_api.model.dto.category.CategoryRequest;
import com.example.product_api.model.dto.category.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse add(CategoryRequest request);
    CategoryResponse update(CategoryRequest request, Long id);
    CategoryResponse getById(Long id);
    List<CategoryResponse> all();
}
