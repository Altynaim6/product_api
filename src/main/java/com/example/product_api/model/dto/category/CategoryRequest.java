package com.example.product_api.model.dto.category;

import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private String description;
}