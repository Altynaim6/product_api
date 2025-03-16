package com.example.product_api.mapper;

import com.example.product_api.model.domain.Product;
import com.example.product_api.model.dto.product.ProductRequest;
import com.example.product_api.model.dto.product.ProductResponse;

import java.util.List;

public interface ProductMapper {
    Product toProduct(Product product, ProductRequest request);
    ProductResponse toResponse(Product product);
    List<ProductResponse> toResponseList(List<Product> products);
}
