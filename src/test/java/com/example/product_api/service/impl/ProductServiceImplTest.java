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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private User seller;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        seller = new User();
        seller.setId(1L);
        seller.setRole(Role.SELLER);

        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("Gaming laptop");
        product.setPrice(1200.0);
        product.setQuantity(10);
        product.setSeller(seller);
        product.setCategory(category);

        productRequest = new ProductRequest();
        productRequest.setName("Laptop");
        productRequest.setDescription("Gaming laptop");
        productRequest.setPrice(1200.0);
        productRequest.setQuantity(10.0);

        productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Laptop");
        productResponse.setDescription("Gaming laptop");
        productResponse.setPrice(1200.0);
        productResponse.setQuantity(10.0);
    }

    @Test
    void all_ShouldReturnProductList() {
        Page<Product> page = new PageImpl<>(Collections.singletonList(product));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(productMapper.toResponseList(anyList())).thenReturn(Collections.singletonList(productResponse));

        List<ProductResponse> result = productService.all(0, 10);

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
        verify(productRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void sellerProducts_ShouldReturnProductsOfSeller() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(productRepository.findAllBySeller(eq(seller), any(PageRequest.class)))
                .thenReturn(Collections.singletonList(product));
        when(productMapper.toResponseList(anyList())).thenReturn(Collections.singletonList(productResponse));

        List<ProductResponse> result = productService.sellerProducts(1L, 0, 10);

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
        verify(productRepository, times(1)).findAllBySeller(eq(seller), any(PageRequest.class));
    }

    @Test
    void sellerProducts_ShouldThrowException_WhenSellerNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.sellerProducts(1L, 0, 10);
        });

        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void addProduct_ShouldReturnProductResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productMapper.toProduct(any(Product.class), eq(productRequest))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        ProductResponse result = productService.addProduct(1L, 1L, productRequest);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addProduct_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.addProduct(1L, 1L, productRequest);
        });

        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void addProduct_ShouldThrowException_WhenUserIsNotSeller() {
        seller.setRole(Role.SELLER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(seller));

        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.addProduct(1L, 1L, productRequest);
        });

        assertEquals("You are not able to create product", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void getById_ShouldReturnProductResponse() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        ProductResponse result = productService.getById(1L);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.getById(1L);
        });

        assertEquals("Product not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void update_ShouldReturnUpdatedProductResponse() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toProduct(any(Product.class), eq(productRequest))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        ProductResponse result = productService.update(1L, 1L, productRequest);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void update_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.update(1L, 1L, productRequest);
        });

        assertEquals("Product not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void delete_ShouldCallRepositoryDeleteById() {
        doNothing().when(productRepository).deleteById(1L);

        productService.delete(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
}