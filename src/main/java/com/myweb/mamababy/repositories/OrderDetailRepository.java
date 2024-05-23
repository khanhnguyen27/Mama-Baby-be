package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> {

    List<OrderDetail> findByOderID(int order_id);
}
