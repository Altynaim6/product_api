package com.example.product_api.model.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryResponse {

    @NotNull(message = "Category ID cannot be null")
    private Long id;

    @NotNull(message = "Category name cannot be null")
    @Size(min = 3, max = 100, message = "Category name must be between 3 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;
}