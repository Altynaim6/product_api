package com.example.product_api.mapper;

import com.example.product_api.model.domain.Category;
import com.example.product_api.model.dto.category.CategoryRequest;
import com.example.product_api.model.dto.category.CategoryResponse;

import java.util.List;

public interface CategoryMapper {
    Category toCategory(Category category, CategoryRequest request);
    CategoryResponse toResponse(Category category);
    List<CategoryResponse> toResponseList(List<Category> categories);
}