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
        // Prepare test data
        seller = new User();
        seller.setName("Seller One");
        seller.setEmail("seller@domain.com");
        seller.setPassword("password123");
        userRepository.save(seller);

        category = new Category();
        category.setName("Electronics");
        category.setDescription("Gadgets and devices");
        categoryRepository.save(category);

        product1 = new Product();
        product1.setName("Laptop");
        product1.setDescription("High performance laptop");
        product1.setPrice(1200.0);
        product1.setQuantity(10);
        product1.setSeller(seller);
        product1.setCategory(category);
        productRepository.save(product1);

        product2 = new Product();
        product2.setName("Smartphone");
        product2.setDescription("Latest model smartphone");
        product2.setPrice(800.0);
        product2.setQuantity(20);
        product2.setSeller(seller);
        product2.setCategory(category);
        productRepository.save(product2);

        product3 = new Product();
        product3.setName("Tablet");
        product3.setDescription("Portable and powerful tablet");
        product3.setPrice(600.0);
        product3.setQuantity(15);
        product3.setSeller(seller);
        product3.setCategory(category);
        productRepository.save(product3);
    }

    @Test
    void findAllBySeller() {
        // Test retrieving products by seller
        List<Product> products = productRepository.findAllBySeller(seller, PageRequest.of(0, 10));
        assertEquals(3, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
        assertTrue(products.contains(product3));
    }

    @Test
    void findAllByCategory() {
        // Test retrieving products by category
        List<Product> products = productRepository.findAllByCategory(category, PageRequest.of(0, 10));
        assertEquals(3, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
        assertTrue(products.contains(product3));
    }

    @Test
    void sortByDate() {
        // Test sorting products by createdAt date
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);

        List<Product> products = productRepository.sortByDate(startDate, endDate, PageRequest.of(0, 10));
        assertNotNull(products);
        assertTrue(products.size() > 0);
        // Check that products are sorted by createdAt in descending order
        assertTrue(products.get(0).getCreatedAt().isAfter(products.get(1).getCreatedAt()));
    }
}