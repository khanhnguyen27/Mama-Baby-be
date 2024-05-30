package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserId(int userId);

    List<Order> findByStoreId(int storeId);

    @Query("SELECT o FROM Order o WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(o.type) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.shippingAddress) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "STR(o.orderDate) LIKE %:keyword%)")
    Page<Order> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}