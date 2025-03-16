package com.example.product_api.model.dto.product;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private Double quantity;
}