package com.myweb.mamababy.services.Order;

import com.myweb.mamababy.dtos.CartItemDTO;
import com.myweb.mamababy.dtos.OrderDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.models.OrderDetail;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.models.Voucher;
import com.myweb.mamababy.repositories.OrderDetailRepository;
import com.myweb.mamababy.repositories.OrderRepository;
import com.myweb.mamababy.repositories.ProductRepository;
import com.myweb.mamababy.repositories.StoreRepository;
import com.myweb.mamababy.repositories.UserRepository;
import com.myweb.mamababy.repositories.VoucherRepository;
import jakarta.transaction.Transactional;
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

    @Override
    @Transactional
    public List<Order> createOrder(OrderDTO orderDTO) throws Exception {

        User existingUser = userRepository.findById(orderDTO.getUserId())
            .orElseThrow(() -> new DataNotFoundException(
                "Cannot find user with id: " + orderDTO.getUserId()));

        Voucher existingVoucher = voucherRepository.findById(orderDTO.getVoucherId())
            .orElseThrow(() -> new DataNotFoundException(
                "Cannot find voucher with id: " + orderDTO.getVoucherId()));

        if (!existingVoucher.isActive() || !existingUser.getIsActive()) {
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
                .orderDate(orderDTO.getOrderDate())
                .type(orderDTO.getType())
                .user(existingUser)
                .store(existingStore)
                .build();

            orderRepository.save(newOrder);

            List<OrderDetail> orderDetails = new ArrayList<>();

            float totalPrice = 0;

            int totalPoint = 0;

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

                totalPrice += orderDetail.getAmountPrice();
                totalPoint += orderDetail.getAmountPoint();

                orderDetails.add(orderDetail);
            }

            newOrder.setAmount(totalPrice);
            newOrder.setTotalPoint(totalPoint);
            newOrder.setFinalAmount(totalPrice - newOrder.getTotalDiscount());

            orderDetailRepository.saveAll(orderDetails);

            orders.add(newOrder);
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
        Store existingStore = storeRepository.findById(orderDTO.getStoreId())
            .orElseThrow(() -> new DataNotFoundException(
                "Cannot find store with id: " + orderDTO.getStoreId()));

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
