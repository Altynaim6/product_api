package com.example.product_api.model.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data

public class ProductResponse {

    private Long id;

    @NotNull(message = "Product name cannot be null")
    private String name;

    private String description;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be a positive value")
    private Double price;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be a positive value")
    private Double quantity;

    private LocalDateTime createdAt;
}