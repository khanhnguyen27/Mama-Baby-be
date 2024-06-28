package com.myweb.mamababy.services.Order;

import com.myweb.mamababy.dtos.OrderDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Order;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IOrderService {

    List<Order> findByYear(int year) throws DataNotFoundException; ;

    Order createOrder(OrderDTO orderDTO) throws Exception;

    Order getOrder(int id) throws DataNotFoundException;

    Order updateOrder(int id, OrderDTO orderDTO) throws DataNotFoundException;

    List<Order> getAllOrder() throws Exception;

    List<Order> findByUserId(int userId) throws DataNotFoundException;

    List<Order> findByStoreId(int storeId) throws DataNotFoundException;

    Page<Order> getOrdersByKeyword(String keyword, Pageable pageable) throws DataNotFoundException;

}
