package com.example.product_api.controller;

import com.example.product_api.model.dto.product.ProductRequest;
import com.example.product_api.model.dto.product.ProductResponse;
import com.example.product_api.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return productService.all(page, size);
    }

    @GetMapping("/seller/{sellerId}")
    public List<ProductResponse> sellerProducts(
            @PathVariable Long sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return productService.sellerProducts(sellerId, page, size);
    }

    @GetMapping("/sort_category")
    public List<ProductResponse> sortByCategory(
            @RequestParam(name = "category") String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return productService.sortByCategory(categoryName, page, size);
    }

    @GetMapping("/sort_date")
    public List<ProductResponse> sortByDate(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return productService.sortByDate(startDate, endDate, page, size);
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PostMapping("/seller/{sellerId}/category/{categoryId}")
    public ProductResponse addProduct(
            @PathVariable Long sellerId,
            @PathVariable Long categoryId,
            @RequestBody ProductRequest productRequest
    ) {
        return productService.addProduct(sellerId, categoryId, productRequest);
    }

    @PutMapping("/{id}")
    public ProductResponse update(
            @PathVariable Long id,
            @RequestParam(required = false) Long categoryId,
            @RequestBody ProductRequest productRequest
    ) {
        return productService.update(id, categoryId, productRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
