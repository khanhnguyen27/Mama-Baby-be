package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    Optional<Product> findById(int id);

    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword%) " +
            "AND (:type IS NULL OR :type = '' OR p.type LIKE %:type%) " +
            "AND (:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) " +
            "AND (:brandId IS NULL OR :brandId = 0 OR p.brand.id = :brandId) " +
            "AND (:storeId IS NULL OR :storeId = 0 OR p.store.id = :storeId) " +
            "AND (:age IS NULL OR :age = 0 OR p.age.id = :age) " +
            "AND p.isActive = true " +
            "AND p.expiry_date > current_date")
    Page<Product> searchProducts
            (@Param("keyword") String keyword,
             @Param("type") String type,
             @Param("categoryId") int categoryId,
             @Param("brandId") int brandId,
             @Param("age") int age,
             @Param("storeId") int storeId,
              Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(:type IS NULL OR :type = '' OR p.type LIKE %:type%) " +
            "AND p.isActive = true ")
    List<Product> searchProductsCH(@Param("type") String type);

    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword%) " +
            "AND (:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) " +
            "AND (:brandId IS NULL OR :brandId = 0 OR p.brand.id = :brandId)" +
            "AND (:storeId IS NULL OR :storeId = 0 OR p.store.id = :storeId)" +
            "AND (:age IS NULL OR :age = 0 OR p.age.id = :age) ")
    Page<Product> searchProductsByStore
            (@Param("keyword") String keyword,
             @Param("categoryId") int categoryId,
             @Param("brandId") int brandId,
             @Param("age") int age,
             @Param("storeId") int storeId,
             Pageable pageable);

    List<Product> findByStoreId(int id);
}
