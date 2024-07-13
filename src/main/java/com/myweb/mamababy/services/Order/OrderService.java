package com.myweb.mamababy.services.Order;

import com.myweb.mamababy.dtos.CartItemDTO;
import com.myweb.mamababy.dtos.OrderDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.exceptions.InvalidParamException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final StatusOrderRepository statusOrderRepository;

    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        User existingUser = userRepository.findById(orderDTO.getUserId())
            .orElseThrow(() -> new DataNotFoundException(
                "Cannot find user with id: " + orderDTO.getUserId()));
        Store existingStore = storeRepository.findById(orderDTO.getStoreId())
                .orElseThrow(
                    () -> new DataNotFoundException("Cannot find store with id: " + orderDTO.getStoreId()));
        Voucher existingVoucher = null;
        if( orderDTO.getVoucherId() != 0){
             existingVoucher = voucherRepository.findById(orderDTO.getVoucherId())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Cannot find voucher with id: " + orderDTO.getVoucherId()));
        }

        if (!existingUser.getIsActive() || !existingStore.isActive() || existingStore.getValidDate().isBefore(LocalDateTime.now().plusHours(7))) {
            throw new DataNotFoundException("Invalid user, store is inActive");
        }

        Order newOrder = Order.builder()
                .voucher(existingVoucher)
                .totalPoint(orderDTO.getTotalPoint())
                .amount(orderDTO.getAmount())
                .totalDiscount(orderDTO.getTotalDiscount())
                .finalAmount(orderDTO.getFinalAmount())
                .fullName(orderDTO.getFullName())
                .phoneNumber(orderDTO.getPhoneNumber())
                .shippingAddress(orderDTO.getShippingAddress())
                .paymentMethod(orderDTO.getPaymentMethod())
                .orderDate(LocalDateTime.now().plusHours(7))
                .type(orderDTO.getType())
                .user(existingUser)
                .store(existingStore)
                .build();

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);

            int productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + productId));

            // Set product and quantity in order detail.
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);
            orderDetail.setUnitPrice(product.getPrice());
            orderDetail.setUnitPoint(product.getPoint());
            orderDetail.setAmountPrice(orderDetail.getUnitPrice() * orderDetail.getQuantity());
            orderDetail.setAmountPoint(orderDetail.getUnitPoint() * orderDetail.getQuantity());

            orderDetails.add(orderDetail);
        }

        newOrder.setOrderDetails(orderDetails);

        //subtract quantity in product remain
        for (OrderDetail orderDetail : orderDetails) {
            Product existingProduct = productRepository.findById(orderDetail.getProduct().getId())
                    .orElseThrow(() -> new DataNotFoundException
                            ("Cannot find product with id: " + orderDetail.getProduct().getId()));

            int quantityCurrent = existingProduct.getRemain() - orderDetail.getQuantity();
            if (quantityCurrent < 0) {
                quantityCurrent = 0;
            }
            if(!existingProduct.getStatus().equals("COMING SOON"))
            {
                existingProduct.setRemain(quantityCurrent);
            }
            if(quantityCurrent == 0 && !existingProduct.getStatus().equals("COMING SOON")){
                existingProduct.setStatus("OUT OF STOCK");
            }

            productRepository.save(existingProduct);
        }

        //subtract point user
        if(orderDTO.getTotalPoint() != 0 ){
            if(orderDTO.getTotalPoint() > existingUser.getAccumulatedPoints()){
                throw new InvalidParamException("Not enough point !!!");
            }
            existingUser.setAccumulatedPoints(existingUser.getAccumulatedPoints() - orderDTO.getTotalPoint());
            userRepository.save(existingUser);
        }

        //  Set status for order
            List<StatusOrder> statusOrders = new ArrayList<>();
            if(orderDTO.getPaymentMethod().equals("VNPAY")){
                statusOrders.add(StatusOrder.builder()
                                .order(newOrder)
                                .date(LocalDateTime.now().plusHours(7))
                                .status("UNPAID")
                                .build());
            }else{
                statusOrders.add(StatusOrder.builder()
                        .order(newOrder)
                        .date(LocalDateTime.now().plusHours(7))
                        .status("PENDING")
                        .build());
            }

        newOrder.setStatusOrders(statusOrders);

        //  Set active Voucher
        if(existingVoucher != null){
            activedRepository.save(Actived.builder()
                            .userId(existingUser.getId())
                            .voucherId(existingVoucher.getId())
                            .build());
        }



        orderRepository.save(newOrder);
        statusOrderRepository.saveAll(statusOrders);
        orderDetailRepository.saveAll(orderDetails);
    return newOrder;
    }

    @Override
    public Order getOrder(int id) throws DataNotFoundException {
        return orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + id));
    }

    @Override
    @Transactional
    public Order updateOrder(int id, OrderDTO orderDTO) throws DataNotFoundException {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException("Cannot find order with id: " + id));

        existingOrder.setType(orderDTO.getType());

        return orderRepository.save(existingOrder);
    }

    @Override
    public List<Order> getAllOrder(){
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

    @Override
    public List<Order> findByYear(int year) {
        List<Order> orders = orderRepository.findByOrderDateYear(year);
        return orders.stream()
                .filter(order -> order.getOrderDate().getYear() == year)
                .collect(Collectors.toList()); // Removed .orElseThrow(...)
    }
}
