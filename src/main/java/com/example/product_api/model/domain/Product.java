package com.example.product_api.model.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "product_tb")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private double quantity;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn
    private User seller;

    @ManyToOne
    @JoinColumn
    private Category category;
}