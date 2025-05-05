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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProduct_shouldCreateProductSuccessfully() {
        // given
        Long sellerId = 1L;
        Long categoryId = 2L;

        ProductRequest request = new ProductRequest();
        // при необходимости, установи нужные поля, например: request.setName("test product");

        User seller = new User();
        seller.setId(sellerId);
        seller.setRole(Role.SELLER);

        Category category = new Category();
        category.setId(categoryId);

        Product product = new Product();
        Product savedProduct = new Product();
        ProductResponse response = new ProductResponse();

        when(userRepository.findById(sellerId)).thenReturn(java.util.Optional.of(seller));
        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.of(category));
        when(productMapper.toProduct(any(Product.class), eq(request))).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toResponse(savedProduct)).thenReturn(response);

        // when
        ProductResponse actual = productService.addProduct(sellerId, categoryId, request);

        // then
        assertEquals(response, actual);
        verify(userRepository).findById(sellerId);
        verify(categoryRepository).findById(categoryId);
        verify(productMapper).toProduct(any(Product.class), eq(request));
        verify(productRepository).save(product);
        verify(productMapper).toResponse(savedProduct);
    }

    @Test
    void addProduct_shouldThrowExceptionIfUserNotSeller() {
        // given
        Long sellerId = 1L;
        Long categoryId = 2L;

        ProductRequest request = new ProductRequest();
        User user = new User();
        user.setRole(Role.CUSTOMER); // не продавец

        when(userRepository.findById(sellerId)).thenReturn(java.util.Optional.of(user));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () ->
                productService.addProduct(sellerId, categoryId, request)
        );

        assertEquals("You are not able to create product", exception.getMessage());
        assertEquals(400, exception.getStatus().value());
    }
}