package com.example.product_api.service;

import com.example.product_api.model.dto.product.ProductRequest;
import com.example.product_api.model.dto.product.ProductResponse;

import java.time.LocalDate;
import java.util.List;

public interface ProductService {
    List<ProductResponse> all(int page, int size);
    List<ProductResponse> sellerProducts(Long sellerId, int page, int size);
    List<ProductResponse> sortByCategory(String categoryName, int page, int size);
    List<ProductResponse> sortByDate(LocalDate startDate, LocalDate endDate, int page, int size);
    ProductResponse getById(Long id);
    ProductResponse addProduct(Long sellerId, Long categoryId, ProductRequest productRequest);
    ProductResponse update(Long id, Long categoryId,ProductRequest productRequest);
    void delete(Long id);
}