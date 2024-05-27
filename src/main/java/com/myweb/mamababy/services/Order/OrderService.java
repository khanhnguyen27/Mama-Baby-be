package com.myweb.mamababy.services.Order;

import com.myweb.mamababy.dtos.OrderDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.models.Voucher;
import com.myweb.mamababy.repositories.OrderRepository;
import com.myweb.mamababy.repositories.UserRepository;
import com.myweb.mamababy.repositories.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final VoucherRepository voucherRepository;

    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) throws Exception {

        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));

        Voucher existingVoucher = voucherRepository.findById(orderDTO.getVoucherId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find voucher with id: " + orderDTO.getVoucherId()));

        Order newOrder = Order.builder()

                .voucher(existingVoucher)
                .totalPoint(orderDTO.getTotalPoint())
                .amount(orderDTO.getAmount())
                .totalDiscount(orderDTO.getTotalDiscount())
                .finalAmount(orderDTO.getFinalAmount())
                .shippingAddress(orderDTO.getShippingAddress())
                .paymentMethod(orderDTO.getPaymentMethod())
                .orderDate(orderDTO.getOrderDate())
                .type(orderDTO.getType())
                .user(existingUser)
                .build();

        return orderRepository.save(newOrder);

    }

    @Override
    public Order getOrder(int id) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + id));
        return  order;
    }

    @Override
    public Order updateOrder(int id, OrderDTO orderDTO) throws DataNotFoundException {


        User existingUser = userRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find user with id: " + id));

        Voucher existingVoucher  = voucherRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find voucher with id: " + id));

        Order existingOrder = orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find order with id: " + id));

        existingOrder.setTotalPoint(orderDTO.getTotalPoint());
        existingOrder.setAmount(orderDTO.getAmount());
        existingOrder.setTotalDiscount(orderDTO.getTotalDiscount());
        existingOrder.setFinalAmount(orderDTO.getFinalAmount());
        existingOrder.setShippingAddress(orderDTO.getShippingAddress());
        existingOrder.setPaymentMethod(orderDTO.getPaymentMethod());
        existingOrder.setOrderDate(orderDTO.getOrderDate());
        existingOrder.setType(orderDTO.getType());

        existingOrder.setUser(existingUser);
        existingOrder.setVoucher(existingVoucher);

        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(int id) {
        //Hard Delete
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> findByUserId(int userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Page<Order> getOrdersByKeyword(String keyword, Pageable pageable) {
 //     return orderRepository.findByKeyword(keyword, pageable);
      return null;
    }
}
