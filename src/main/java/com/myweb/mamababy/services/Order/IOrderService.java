package com.myweb.mamababy.services.Order;

import com.myweb.mamababy.dtos.OrderDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IOrderService {

    Order createOrder(OrderDTO orderDTO) throws Exception;

    Order getOrder(int id) throws DataNotFoundException;

    Order updateOrder(int id, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(int id);

    List<Order> findByUserId(int userId);

    Page<Order> getOrdersByKeyword(String keyword, Pageable pageable);

}
