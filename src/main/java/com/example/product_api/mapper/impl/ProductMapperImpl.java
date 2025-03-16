package com.example.product_api.mapper.impl;


import com.example.product_api.mapper.ProductMapper;
import com.example.product_api.model.domain.Product;
import com.example.product_api.model.dto.product.ProductRequest;
import com.example.product_api.model.dto.product.ProductResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapperImpl implements ProductMapper {
    @Override
    public Product toProduct(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCreatedAt(LocalDateTime.now());
        return product;
    }

    @Override
    public ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setCreatedAt(product.getCreatedAt());
        return response;
    }

    @Override
    public List<ProductResponse> toResponseList(List<Product> products) {
        List<ProductResponse> responses = new ArrayList<>();
        for (Product product : products) {
            responses.add(toResponse(product));
        }
        return responses;
    }
}