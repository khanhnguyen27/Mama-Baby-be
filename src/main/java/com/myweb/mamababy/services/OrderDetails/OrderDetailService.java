package com.myweb.mamababy.services.OrderDetails;

import com.myweb.mamababy.dtos.OrderDetailDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.models.OrderDetail;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.repositories.OrderDetailRepository;
import com.myweb.mamababy.repositories.OrderRepository;
import com.myweb.mamababy.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception {

        Order  existingorder = orderRepository.findById(newOrderDetail.getOderId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find Order with id : " + newOrderDetail.getOderId()));

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

        Order existingOrder = orderRepository.findById(newOrderDetailData.getOderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + newOrderDetailData.getOderId()));

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
    public void deleteById(int id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public Optional<OrderDetail> findByOrderId(int orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
