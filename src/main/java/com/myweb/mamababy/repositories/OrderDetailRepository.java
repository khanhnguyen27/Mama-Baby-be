package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    Optional<OrderDetail> findByOrderId(int orderId);
}
