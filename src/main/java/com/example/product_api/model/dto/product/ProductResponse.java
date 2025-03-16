package com.example.product_api.model.dto.product;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Double quantity;
    private LocalDateTime createdAt;
}