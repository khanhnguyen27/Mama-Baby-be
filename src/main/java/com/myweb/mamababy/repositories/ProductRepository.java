package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Category;
import com.myweb.mamababy.models.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    List<Product> findByCategory(Category category);

    Optional<Product> findById(int id);

    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword%) " +
            "AND (:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) " +
            "AND (:brandId IS NULL OR :brandId = 0 OR p.brand.id = :brandId)" +
            "AND (:age IS NULL OR :age = 0 OR p.age.id = :age)")
    Page<Product> searchProducts
            (@Param("keyword") String keyword,
             @Param("categoryId") int categoryId,
             @Param("brandId") int brandId,
             @Param("age") int age,
              Pageable pageable);

}
