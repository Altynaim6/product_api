package com.example.product_api.mapper.impl;

import com.example.product_api.model.domain.Product;
import com.example.product_api.model.dto.product.ProductRequest;
import com.example.product_api.model.dto.product.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperImplTest {

    private ProductMapperImpl productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapperImpl();
    }

    @Test
    void toProduct() {
        Product product = new Product();
        ProductRequest request = new ProductRequest();
        request.setName("Laptop");
        request.setDescription("High-performance laptop");
        request.setPrice(1200.0);
        request.setQuantity(5.0);

        Product mappedProduct = productMapper.toProduct(product, request);

        assertNotNull(mappedProduct);
        assertEquals("Laptop", mappedProduct.getName());
        assertEquals("High-performance laptop", mappedProduct.getDescription());
        assertEquals(1200.0, mappedProduct.getPrice());
        assertEquals(5.0, mappedProduct.getQuantity());
        assertNotNull(mappedProduct.getCreatedAt()); // Ensure createdAt is set
    }

    @Test
    void toResponse() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Smartphone");
        product.setDescription("Latest model smartphone");
        product.setPrice(800.0);
        product.setQuantity(10.0);
        product.setCreatedAt(LocalDateTime.now());

        ProductResponse response = productMapper.toResponse(product);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Smartphone", response.getName());
        assertEquals("Latest model smartphone", response.getDescription());
        assertEquals(800.0, response.getPrice());
        assertEquals(10.0, response.getQuantity());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    void toResponseList() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Tablet");
        product1.setDescription("Lightweight tablet");
        product1.setPrice(500.0);
        product1.setQuantity(7.0);
        product1.setCreatedAt(LocalDateTime.now());

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Monitor");
        product2.setDescription("4K resolution monitor");
        product2.setPrice(300.0);
        product2.setQuantity(4.0);
        product2.setCreatedAt(LocalDateTime.now());

        List<ProductResponse> responses = productMapper.toResponseList(List.of(product1, product2));

        assertNotNull(responses);
        assertEquals(2, responses.size());

        assertEquals(1L, responses.get(0).getId());
        assertEquals("Tablet", responses.get(0).getName());
        assertEquals("Lightweight tablet", responses.get(0).getDescription());
        assertEquals(500.0, responses.get(0).getPrice());
        assertEquals(7.0, responses.get(0).getQuantity());
        assertNotNull(responses.get(0).getCreatedAt());

        assertEquals(2L, responses.get(1).getId());
        assertEquals("Monitor", responses.get(1).getName());
        assertEquals("4K resolution monitor", responses.get(1).getDescription());
        assertEquals(300.0, responses.get(1).getPrice());
        assertEquals(4.0, responses.get(1).getQuantity());
        assertNotNull(responses.get(1).getCreatedAt());
    }
}