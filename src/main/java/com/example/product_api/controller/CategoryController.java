package com.example.product_api.controller;

import com.example.product_api.model.dto.category.CategoryRequest;
import com.example.product_api.model.dto.category.CategoryResponse;
import com.example.product_api.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private CategoryService categoryService;

    @PostMapping
    public CategoryResponse add(@RequestBody CategoryRequest request) {
        return categoryService.add(request);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(
            @RequestBody CategoryRequest request,
            @PathVariable Long id
    ) {
        return categoryService.update(request, id);
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @GetMapping
    public List<CategoryResponse> all() {
        return categoryService.all();
    }
}