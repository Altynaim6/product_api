package com.example.product_api.controller;

import com.example.product_api.model.dto.product.ProductRequest;
import com.example.product_api.model.dto.product.ProductResponse;
import com.example.product_api.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        productRequest = new ProductRequest();
        productRequest.setName("Laptop");
        productRequest.setDescription("Gaming Laptop");
        productRequest.setPrice(1500.00);
        productRequest.setQuantity(5.0);

        productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Laptop");
        productResponse.setDescription("Gaming Laptop");
        productResponse.setPrice(1500.00);
        productResponse.setQuantity(5.0);
        productResponse.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void all_ShouldReturnProductList() throws Exception {
        when(productService.all(0, 10)).thenReturn(List.of(productResponse));

        mockMvc.perform(get("/products?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].description").value("Gaming Laptop"));

        verify(productService).all(0, 10);
    }

    @Test
    void sellerProducts_ShouldReturnSellerProductList() throws Exception {
        when(productService.sellerProducts(1L, 0, 10)).thenReturn(List.of(productResponse));

        mockMvc.perform(get("/products/seller/1?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Laptop"));

        verify(productService).sellerProducts(1L, 0, 10);
    }

    @Test
    void sortByCategory_ShouldReturnSortedProducts() throws Exception {
        when(productService.sortByCategory("Electronics", 0, 10)).thenReturn(List.of(productResponse));

        mockMvc.perform(get("/products/sort_category?category=Electronics&page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Laptop"));

        verify(productService).sortByCategory("Electronics", 0, 10);
    }

    @Test
    void sortByDate_ShouldReturnProductsInDateRange() throws Exception {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        when(productService.sortByDate(startDate, endDate, 0, 10)).thenReturn(List.of(productResponse));

        mockMvc.perform(get("/products/sort_date?startDate=2024-01-01&endDate=2024-12-31&page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Laptop"));

        verify(productService).sortByDate(startDate, endDate, 0, 10);
    }

    @Test
    void getById_ShouldReturnProduct() throws Exception {
        when(productService.getById(1L)).thenReturn(productResponse);

        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"));

        verify(productService).getById(1L);
    }

    @Test
    void addProduct_ShouldReturnCreatedProduct() throws Exception {
        when(productService.addProduct(eq(1L), eq(2L), any(ProductRequest.class))).thenReturn(productResponse);

        mockMvc.perform(post("/products/seller/1/category/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"));

        verify(productService).addProduct(eq(1L), eq(2L), any(ProductRequest.class));
    }

    @Test
    void update_ShouldReturnUpdatedProduct() throws Exception {
        when(productService.update(eq(1L), eq(2L), any(ProductRequest.class))).thenReturn(productResponse);

        mockMvc.perform(put("/products/1?categoryId=2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"));

        verify(productService).update(eq(1L), eq(2L), any(ProductRequest.class));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService).delete(1L);
    }
}