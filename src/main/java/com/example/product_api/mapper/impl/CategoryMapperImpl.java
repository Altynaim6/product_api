package com.example.product_api.mapper.impl;

import com.example.product_api.model.dto.category.CategoryResponse;
import com.example.product_api.mapper.CategoryMapper;
import com.example.product_api.model.domain.Category;
import com.example.product_api.model.dto.category.CategoryRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapperImpl implements CategoryMapper {
    @Override
    public Category toCategory(Category category, CategoryRequest request) {
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return category;
    }

    @Override
    public CategoryResponse toResponse(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setDescription(category.getDescription());
        return categoryResponse;
    }

    @Override
    public List<CategoryResponse> toResponseList(List<Category> categories) {
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categories) {
            categoryResponses.add(toResponse(category));
        }
        return categoryResponses;
    }
}