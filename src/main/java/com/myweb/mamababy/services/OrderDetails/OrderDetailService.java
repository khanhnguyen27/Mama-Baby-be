package com.myweb.mamababy.services.OrderDetails;

import com.myweb.mamababy.dtos.OrderDetailDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.models.OrderDetail;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.repositories.OrderDetailRepository;
import com.myweb.mamababy.repositories.OrderRepository;
import com.myweb.mamababy.repositories.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception {

        Order  existingorder = orderRepository.findById(newOrderDetail.getOrderId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find Order with id : " + newOrderDetail.getOrderId()));

        Product existingproduct = productRepository.findById(newOrderDetail.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id: " + newOrderDetail.getProductId()));

        OrderDetail orderDetail = OrderDetail.builder()
                .order(existingorder)
                .product(existingproduct)
                .quantity(newOrderDetail.getQuantity())
                .unitPrice(newOrderDetail.getUnitPrice())
                .unitPoint(newOrderDetail.getUnitPoint())
                .amountPrice(newOrderDetail.getAmountPrice())
                .amountPoint(newOrderDetail.getAmountPoint())
                .build();
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(int id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find OrderDetail with id: " + id));
    }

    @Override
    public OrderDetail updateOrderDetail(int id, OrderDetailDTO newOrderDetailData) throws DataNotFoundException {


        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order detail with id: " + id));

        Order existingOrder = orderRepository.findById(newOrderDetailData.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + newOrderDetailData.getOrderId()));

        Product existingProduct = productRepository.findById(newOrderDetailData.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id: " + newOrderDetailData.getProductId()));

        existingOrderDetail.setUnitPrice(newOrderDetailData.getUnitPrice());
        existingOrderDetail.setUnitPoint(newOrderDetailData.getUnitPoint());
        existingOrderDetail.setQuantity(newOrderDetailData.getQuantity());
        existingOrderDetail.setAmountPrice(newOrderDetailData.getAmountPrice());
        existingOrderDetail.setAmountPrice(newOrderDetailData.getAmountPoint());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);

        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public List<OrderDetail> getAllOrderDetail() throws Exception {
        return orderDetailRepository.findAll();
    }

    @Override
    public List<OrderDetail> findByOrderId(int orderId) throws DataNotFoundException {

        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);

        if (orderDetails.isEmpty()) {
            throw new DataNotFoundException("Cannot find orderDetail for oder with id: " + orderId);
        }

        return orderDetails;
    }
}
