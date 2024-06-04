package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.StatusOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusOrderRepository extends JpaRepository<StatusOrder, Integer> {

  List<StatusOrder> findByOrderId(int orderID);

  List<StatusOrder> findByStatus(String status);
}
