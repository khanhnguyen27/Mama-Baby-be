package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.models.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefundRepository extends JpaRepository<Refund, Integer> {
    List<Refund> findByUserId(int userId);
}
