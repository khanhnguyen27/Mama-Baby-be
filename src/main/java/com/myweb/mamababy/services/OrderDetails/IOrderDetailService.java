package com.myweb.mamababy.services.OrderDetails;

import com.myweb.mamababy.dtos.OrderDetailDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.OrderDetail;
import java.util.List;

public interface IOrderDetailService {

    OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception;

    OrderDetail getOrderDetail(int id) throws DataNotFoundException;

    OrderDetail updateOrderDetail(int id, OrderDetailDTO newOrderDetailData)
            throws DataNotFoundException;

    List<OrderDetail> getAllOrderDetail() throws Exception;

    List<OrderDetail> findByOrderId(int orderId) throws DataNotFoundException;
}