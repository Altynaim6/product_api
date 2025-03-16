package com.example.product_api.repository;

import com.example.product_api.model.domain.Category;
import com.example.product_api.model.domain.Product;
import com.example.product_api.model.domain.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllBySeller(User seller, PageRequest of);

    List<Product> findAllByCategory(Category category, PageRequest of);

    @Query(
            value = "SELECT * FROM product_tb " +
                    "WHERE created_at BETWEEN :startDate AND :endDate " +
                    "ORDER BY created_at DESC",
            nativeQuery = true
    )
    List<Product> sortByDate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            PageRequest of
    );
}