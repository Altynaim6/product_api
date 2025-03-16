package com.example.product_api.service.impl;

import com.example.product_api.exception.CustomException;
import com.example.product_api.mapper.ProductMapper;
import com.example.product_api.model.domain.Category;
import com.example.product_api.model.domain.Product;
import com.example.product_api.model.domain.User;
import com.example.product_api.model.dto.product.ProductRequest;
import com.example.product_api.model.dto.product.ProductResponse;
import com.example.product_api.model.enums.Role;
import com.example.product_api.repository.CategoryRepository;
import com.example.product_api.repository.ProductRepository;
import com.example.product_api.repository.UserRepository;
import com.example.product_api.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> all(int page, int size) {
        return productMapper.toResponseList(productRepository.findAll(PageRequest.of(page, size)).getContent());
    }

    @Override
    public List<ProductResponse> sellerProducts(Long sellerId, int page, int size) {
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        return productMapper.toResponseList(productRepository.findAllBySeller(seller, PageRequest.of(page, size)));
    }

    @Override
    public List<ProductResponse> sortByCategory(String categoryName, int page, int size) {
        Category category = categoryRepository.findByName(categoryName).orElseThrow(() -> new CustomException("Category not found", HttpStatus.NOT_FOUND));
        return productMapper.toResponseList(productRepository.findAllByCategory(category, PageRequest.of(page, size)));
    }

    @Override
    public List<ProductResponse> sortByDate(LocalDate startDate, LocalDate endDate, int page, int size) {
        return productMapper.toResponseList(productRepository.sortByDate(startDate, endDate, PageRequest.of(page, size)));
    }

    @Override
    public ProductResponse getById(Long id) {
        return productMapper.toResponse(productRepository.findById(id).orElseThrow(() -> new CustomException("Product not found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public ProductResponse addProduct(Long sellerId, Long categoryId, ProductRequest productRequest) {
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        if (!seller.getRole().equals(Role.SELLER)) {
            throw new CustomException("You are not able to create product", HttpStatus.BAD_REQUEST);
        }
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CustomException("Category not found", HttpStatus.NOT_FOUND));
        Product product = productMapper.toProduct(new Product(), productRequest);
        product.setSeller(seller);
        product.setCategory(category);
        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse update(Long id, Long categoryId, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow(() -> new CustomException("Product not found", HttpStatus.NOT_FOUND));
        product = productMapper.toProduct(product, productRequest);
        if (!(categoryId == null)) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CustomException("Category not found", HttpStatus.NOT_FOUND));
            product.setCategory(category);
        }
        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
