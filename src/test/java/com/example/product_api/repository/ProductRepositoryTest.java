package com.example.product_api.repository;

import com.example.product_api.model.domain.Category;
import com.example.product_api.model.domain.Product;
import com.example.product_api.model.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User seller;
    private Category category;
    private Product product1, product2, product3;

    @BeforeEach
    void setUp() {
        // Create and save a seller
        seller = new User();
        seller.setName("John Doe");
        seller.setRole(com.example.product_api.model.enums.Role.SELLER);
        seller = userRepository.save(seller);

        // Create and save a category
        category = new Category();
        category.setName("Electronics");
        category.setDescription("Electronic devices");
        category = categoryRepository.save(category);

        // Create and save products
        product1 = new Product();
        product1.setName("Laptop");
        product1.setDescription("Gaming Laptop");
        product1.setPrice(1200.00);
        product1.setQuantity(5.0);
        product1.setSeller(seller);
        product1.setCategory(category);
        product1.setCreatedAt(LocalDateTime.now().minusDays(2));
        productRepository.save(product1);

        product2 = new Product();
        product2.setName("Phone");
        product2.setDescription("Smartphone");
        product2.setPrice(800.00);
        product2.setQuantity(10.0);
        product2.setSeller(seller);
        product2.setCategory(category);
        product2.setCreatedAt(LocalDateTime.now().minusDays(1));
        productRepository.save(product2);

        product3 = new Product();
        product3.setName("Tablet");
        product3.setDescription("Android Tablet");
        product3.setPrice(500.00);
        product3.setQuantity(7.0);
        product3.setSeller(seller);
        product3.setCategory(category);
        product3.setCreatedAt(LocalDateTime.now());
        productRepository.save(product3);
    }

    @Test
    void findAllBySeller_ShouldReturnSellerProducts() {
        List<Product> products = productRepository.findAllBySeller(seller, PageRequest.of(0, 10));
        assertEquals(3, products.size(), "Seller should have 3 products");
    }

    @Test
    void findAllByCategory_ShouldReturnCategoryProducts() {
        List<Product> products = productRepository.findAllByCategory(category, PageRequest.of(0, 10));
        assertEquals(3, products.size(), "Category should contain 3 products");
    }

    @Test
    void sortByDate_ShouldReturnProductsInDescendingOrder() {
        LocalDate startDate = LocalDate.now().minusDays(3);
        LocalDate endDate = LocalDate.now();
        List<Product> products = productRepository.sortByDate(startDate, endDate, PageRequest.of(0, 10));

        assertEquals(3, products.size(), "Should return 3 products");
        assertTrue(products.get(0).getCreatedAt().isAfter(products.get(1).getCreatedAt()));
        assertTrue(products.get(1).getCreatedAt().isAfter(products.get(2).getCreatedAt()));
    }
}