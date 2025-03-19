package com.example.product_api.bootstrap;

import jakarta.annotation.PostConstruct;
import com.example.product_api.model.domain.Category;
import com.example.product_api.model.domain.Product;
import com.example.product_api.model.domain.User;
import com.example.product_api.model.enums.Role;
import com.example.product_api.repository.CategoryRepository;
import com.example.product_api.repository.ProductRepository;
import com.example.product_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @PostConstruct
    public void init() {
        if (userRepository.count() > 0) {
            System.out.println("Database already initialized. Skipping sample data creation.");
            return;
        }

        System.out.println("Initializing database with sample data...");

        // Creating Users
        User user = new User();
        user.setId(null);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("password");
        user.setRole(Role.CUSTOMER);

        User seller1 = new User();
        seller1.setId(null);
        seller1.setName("Alice");
        seller1.setEmail("alice@example.com");
        seller1.setPassword("password");
        seller1.setRole(Role.SELLER);

        User seller2 = new User();
        seller2.setId(null);
        seller2.setName("Bob");
        seller2.setEmail("bob@example.com");
        seller2.setPassword("password");
        seller2.setRole(Role.SELLER);

        User customer1 = new User();
        customer1.setId(null);
        customer1.setName("Charlie");
        customer1.setEmail("charlie@example.com");
        customer1.setPassword("password");
        customer1.setRole(Role.CUSTOMER);

        User customer2 = new User();
        customer2.setId(null);
        customer2.setName("David");
        customer2.setEmail("david@example.com");
        customer2.setPassword("password");
        customer2.setRole(Role.CUSTOMER);

        userRepository.saveAll(List.of(seller1, seller2, customer1, customer2));
        System.out.println("Sellers and Customers successfully created!");

        // Creating Categories
        Category electronics = new Category();
        electronics.setId(null);
        electronics.setName("Electronics");
        electronics.setDescription("Electronic gadgets and devices");

        Category clothing = new Category();
        clothing.setId(null);
        clothing.setName("Clothing");
        clothing.setDescription("Fashion and apparel");

        Category books = new Category();
        books.setId(null);
        books.setName("Books");
        books.setDescription("Various genres of books");

        categoryRepository.saveAll(List.of(electronics, clothing, books));
        System.out.println("Categories successfully created!");

        Product laptop = new Product();
        laptop.setId(null);
        laptop.setName("Laptop");
        laptop.setDescription("High-performance laptop");
        laptop.setPrice(1200.0);
        laptop.setQuantity(10.0);
        laptop.setSeller(seller1);
        laptop.setCategory(electronics);

        Product tshirt = new Product();
        tshirt.setId(null);
        tshirt.setName("T-Shirt");
        tshirt.setDescription("Cotton round-neck t-shirt");
        tshirt.setPrice(20.0);
        tshirt.setQuantity(100.0);
        tshirt.setSeller(seller2);
        tshirt.setCategory(clothing);

        Product novel = new Product();
        novel.setId(null);
        novel.setName("Bestselling Novel");
        novel.setDescription("A gripping fiction novel");
        novel.setPrice(15.0);
        novel.setQuantity(50.0);
        novel.setSeller(seller1);
        novel.setCategory(books);

        productRepository.saveAll(List.of(laptop, tshirt, novel));
        System.out.println("Products successfully created!");
    }
}