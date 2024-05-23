package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserId (int user_id);
}
