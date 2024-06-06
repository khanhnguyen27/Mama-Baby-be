package com.myweb.mamababy.services.Order;

import com.myweb.mamababy.dtos.CartItemDTO;
import com.myweb.mamababy.dtos.OrderDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.*;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final VoucherRepository voucherRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ActivedRepository activedRepository;

    @Override
    @Transactional
    public List<Order> createOrder(OrderDTO orderDTO) throws Exception {

        Voucher existingVoucher = null;

        User existingUser = userRepository.findById(orderDTO.getUserId())
            .orElseThrow(() -> new DataNotFoundException(
                "Cannot find user with id: " + orderDTO.getUserId()));
        if( orderDTO.getVoucherId() != 0){
             existingVoucher = voucherRepository.findById(orderDTO.getVoucherId())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Cannot find voucher with id: " + orderDTO.getVoucherId()));
        }

        if (!existingUser.getIsActive()) {
            throw new DataNotFoundException("Invalid voucher or user is inActive");
        }

        Map<Integer, List<CartItemDTO>> itemsByStore = new HashMap<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            int storeId = cartItemDTO.getStoreId();
            itemsByStore.computeIfAbsent(storeId, ex -> new ArrayList<>()).add(cartItemDTO);
        }

        List<Order> orders = new ArrayList<>();

        for (Map.Entry<Integer, List<CartItemDTO>> entry : itemsByStore.entrySet()) {
            int storeId = entry.getKey();
            List<CartItemDTO> cartItems = entry.getValue();

            Store existingStore = storeRepository.findById(storeId)
                .orElseThrow(
                    () -> new DataNotFoundException("Cannot find store with id: " + storeId));

            if (!existingStore.isActive()) {
                throw new DataNotFoundException("Store with id " + storeId + " is inActive");
            }

            Order newOrder = Order.builder()
                .voucher(existingVoucher)
                .totalPoint(orderDTO.getTotalPoint())
                .amount(orderDTO.getAmount())
                .totalDiscount(orderDTO.getTotalDiscount())
                .finalAmount(orderDTO.getFinalAmount())
                .shippingAddress(orderDTO.getShippingAddress())
                .paymentMethod(orderDTO.getPaymentMethod())
                .orderDate(LocalDate.now())
                .type(orderDTO.getType())
                .user(existingUser)
                .store(existingStore)
                .build();

            List<OrderDetail> orderDetails = new ArrayList<>();

            for (CartItemDTO cartItemDTO : cartItems) {

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(newOrder);

                int productId = cartItemDTO.getProductId();
                int quantity = cartItemDTO.getQuantity();

                Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + productId));

                orderDetail.setProduct(product);
                orderDetail.setQuantity(quantity);
                orderDetail.setUnitPrice(product.getPrice());
                orderDetail.setUnitPoint(product.getPoint());
                orderDetail.setAmountPrice(orderDetail.getUnitPrice() * orderDetail.getQuantity());
                orderDetail.setAmountPoint(orderDetail.getUnitPoint() * orderDetail.getQuantity());

                orderDetails.add(orderDetail);
            }

            //  Set status order table
            List<StatusOrder> statusOrders = new ArrayList<>();
            if(orderDTO.getPaymentMethod().equals("VNPAY")){
                statusOrders.add(StatusOrder.builder()
                                .order(newOrder)
                                .date(LocalDate.now())
                                .status("UNPAID")
                                .build());
            }else{
                statusOrders.add(StatusOrder.builder()
                        .order(newOrder)
                        .date(LocalDate.now())
                        .status("PENDING")
                        .build());
            }


            newOrder.setOrderDetails(orderDetails);
            newOrder.setStatusOrders(statusOrders);
            orderDetailRepository.saveAll(orderDetails);

            orderRepository.save(newOrder);
            orders.add(newOrder);
        }
        //  Set active Voucher
        if(existingVoucher != null){
            activedRepository.save(Actived.builder()
                            .userId(existingUser.getId())
                            .voucherId(existingVoucher.getId())
                            .build());
        }
        return orders;
    }

    @Override
    public Order getOrder(int id) throws DataNotFoundException {
        return orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + id));
    }

    @Override
    @Transactional
    public List<Order> updateOrder(int id, OrderDTO orderDTO) throws DataNotFoundException {

        Order existingOrder = orderRepository.findById(id).orElseThrow(() ->
            new DataNotFoundException("Cannot find order with id: " + id));

        User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(() ->
            new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));
        Voucher existingVoucher = voucherRepository.findById(orderDTO.getVoucherId())
            .orElseThrow(() ->
                new DataNotFoundException(
                    "Cannot find voucher with id: " + orderDTO.getVoucherId()));

        existingOrder.setTotalPoint(orderDTO.getTotalPoint());
        existingOrder.setAmount(orderDTO.getAmount());
        existingOrder.setTotalDiscount(orderDTO.getTotalDiscount());
        existingOrder.setFinalAmount(orderDTO.getFinalAmount());
        existingOrder.setShippingAddress(orderDTO.getShippingAddress());
        existingOrder.setPaymentMethod(orderDTO.getPaymentMethod());
        existingOrder.setType(orderDTO.getType());

        existingOrder.setUser(existingUser);
        existingOrder.setVoucher(existingVoucher);

        Order updatedOrder = orderRepository.save(existingOrder);

        Map<Integer, List<CartItemDTO>> itemsByStore = new HashMap<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            int storeId = cartItemDTO.getStoreId();
            itemsByStore.computeIfAbsent(storeId, k -> new ArrayList<>()).add(cartItemDTO);
        }

        List<OrderDetail> updatedOrderDetails = new ArrayList<>();

        for (Map.Entry<Integer, List<CartItemDTO>> entry : itemsByStore.entrySet()) {
            int storeId = entry.getKey();
            List<CartItemDTO> cartItems = entry.getValue();

            Store store = storeRepository.findById(storeId)
                .orElseThrow(
                    () -> new DataNotFoundException("Cannot find store with id: " + storeId));

            for (CartItemDTO cartItemDTO : cartItems) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(updatedOrder);

                int productId = cartItemDTO.getProductId();
                int quantity = cartItemDTO.getQuantity();

                Product product = productRepository.findById(productId)
                    .orElseThrow(
                        () -> new DataNotFoundException("Product not found with id: " + productId));

                orderDetail.setProduct(product);
                orderDetail.setQuantity(quantity);
                orderDetail.setUnitPrice(product.getPrice());
                orderDetail.setUnitPoint(product.getPoint());
                orderDetail.setAmountPrice(orderDetail.getUnitPrice() * orderDetail.getQuantity());
                orderDetail.setAmountPoint(orderDetail.getUnitPoint() * orderDetail.getQuantity());

                updatedOrderDetails.add(orderDetail);
            }
        }

        orderDetailRepository.deleteById(id);

        orderDetailRepository.saveAll(updatedOrderDetails);

        return Collections.singletonList(updatedOrder);
    }


    @Override
    public List<Order> getAllOrder() throws Exception {
        return orderRepository.findAll();
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
