package com.myweb.mamababy.services.OrderDetails;

import com.myweb.mamababy.dtos.OrderDetailDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.OrderDetail;

import java.util.Optional;

public interface IOrderDetailService {

    OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception;

    OrderDetail getOrderDetail(int id) throws DataNotFoundException;

    OrderDetail updateOrderDetail(int id, OrderDetailDTO newOrderDetailData)
            throws DataNotFoundException;

    void deleteById(int id);

    Optional<OrderDetail> findByOrderId(int orderId);
}