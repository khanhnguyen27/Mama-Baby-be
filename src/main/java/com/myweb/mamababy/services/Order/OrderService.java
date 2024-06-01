package com.myweb.mamababy.services.Order;

import com.myweb.mamababy.dtos.OrderDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.models.Voucher;
import com.myweb.mamababy.repositories.OrderRepository;
import com.myweb.mamababy.repositories.StoreRepository;
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
    private final StoreRepository storeRepository;


    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) throws Exception {

        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));

        Voucher existingVoucher = voucherRepository.findById(orderDTO.getVoucherId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find voucher with id: " + orderDTO.getVoucherId()));

        Store existingStore = storeRepository.findById(orderDTO.getStoreId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find store with id: " + orderDTO.getStoreId()));

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
                .store(existingStore)
                .build();

        return orderRepository.save(newOrder);

    }

    @Override
    public Order getOrder(int id) throws DataNotFoundException {
        return orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + id));
    }

    @Override
    public Order updateOrder(int id, OrderDTO orderDTO) throws DataNotFoundException {

        Order existingOrder = orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find order with id: " + id));

        User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(() ->
                new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));

        Voucher existingVoucher  = voucherRepository.findById(orderDTO.getVoucherId()).orElseThrow(() ->
                new DataNotFoundException("Cannot find voucher with id: " + orderDTO.getVoucherId()));

        Store existingStore = storeRepository.findById(orderDTO.getStoreId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find store with id: " + orderDTO.getStoreId()));

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
        existingOrder.setStore(existingStore);

        return orderRepository.save(existingOrder);
    }

    @Override
    public List<Order> getAllOrder() throws Exception {
        return orderRepository.findAll();
    }

    @Override
    public Order deleteOrder(int id) throws DataNotFoundException {

        Order existingOrder = orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find order with id: " + id));

        orderRepository.deleteById(id);

        return existingOrder;
    }

    @Override
    public List<Order> findByUserId(int userId) throws DataNotFoundException {

        List<Order> orders = orderRepository.findByUserId(userId);

        if (orders.isEmpty()) {
            throw new DataNotFoundException("Cannot find orders for user with id: " + userId);
        }

        return orders;
    }

    @Override
    public List<Order> findByStoreId(int storeId) throws DataNotFoundException {

        List<Order> orders = orderRepository.findByStoreId(storeId);

        if (orders.isEmpty()) {
            throw new DataNotFoundException("Cannot find orders for store with id: " + storeId);
        }

        return orders;
    }

    @Override
    public Page<Order> getOrdersByKeyword(String keyword, Pageable pageable) throws DataNotFoundException {
        Page<Order> orderPage = orderRepository.findByKeyword(keyword, pageable);

        if (orderPage.isEmpty()) {
            throw new DataNotFoundException("No orders found with keyword: " + keyword);
        }

        return orderPage;
    }
}