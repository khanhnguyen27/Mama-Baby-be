package com.myweb.mamababy.services.StatusOrder;

import com.myweb.mamababy.dtos.StatusOrderDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.OrderRepository;
import com.myweb.mamababy.repositories.ProductRepository;
import com.myweb.mamababy.repositories.StatusOrderRepository;
import com.myweb.mamababy.repositories.UserRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusOrderService implements IStatusOrderService {

    private final double pointExchangeRate = (double) 1/10000;

    private final OrderRepository orderRepository;
    private final StatusOrderRepository statusOrderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public StatusOrder createStatusOrder(StatusOrderDTO statusOrderDTO) throws Exception {

        Order existingOrder = orderRepository.findById(statusOrderDTO.getOrderId()).orElseThrow(() ->
                new DataNotFoundException("Cannot find statusOrder with orderId: " + statusOrderDTO.getOrderId()));

        StatusOrder newStatusOrder = StatusOrder.builder()

                .order(existingOrder)
                .status(statusOrderDTO.getStatus())
                .date(LocalDate.now())
                .build();

        if(statusOrderDTO.getStatus().equals("COMPLETED")){
            //gift point if order successful
            User existingUser = userRepository.findById(existingOrder.getUser().getId())
                    .orElseThrow(() -> new DataNotFoundException
                            ("Cannot find user with id: " + existingOrder.getUser().getId()));

            existingUser.setAccumulatedPoints
                    ((int) (existingUser.getAccumulatedPoints() + existingOrder.getFinalAmount()*pointExchangeRate));

            userRepository.save(existingUser);
        }else if(statusOrderDTO.getStatus().equals("CANCELLED")){
            //add remain if order cancelled
            for(OrderDetail orderDetail : existingOrder.getOrderDetails()){
                Product existingProduct = productRepository.findById(orderDetail.getProduct().getId())
                        .orElseThrow(() -> new DataNotFoundException
                                ("Cannot find product with id: " + orderDetail.getProduct().getId()));

                existingProduct.setRemain( existingProduct.getRemain() + orderDetail.getQuantity());

                productRepository.save(existingProduct);
            }
        }

        return statusOrderRepository.save(newStatusOrder);
    }

    @Override
    public StatusOrder updateStatusOrder(int id, StatusOrderDTO statusOrderDTO) throws DataNotFoundException {


        Order existingOrder = orderRepository.findById(statusOrderDTO.getOrderId()).orElseThrow(() ->
                new DataNotFoundException("Cannot find statusOrder with orderId: " + statusOrderDTO.getOrderId()));


        StatusOrder existingStatusOrder = statusOrderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find statusOrder with id: " + id));

        existingStatusOrder.setOrder(existingOrder);
        existingStatusOrder.setStatus(statusOrderDTO.getStatus());
        existingStatusOrder.setDate(LocalDate.now());

        return statusOrderRepository.save(existingStatusOrder);
    }

    @Override
    public List<StatusOrder> getAllStatusOrder() throws Exception {
        return statusOrderRepository.findAll();
    }

    @Override
    public List<StatusOrder> findByOrderId(int orderId) throws DataNotFoundException {

        List<StatusOrder> statusOrder = statusOrderRepository.findByOrderId(orderId);

        if (statusOrder.isEmpty()) {
            throw new DataNotFoundException(
                "Cannot find statusOrder for order with id: " + orderId);
        }

        return statusOrder;
    }

    @Override
    public List<StatusOrder> findbyStatus(String Status) throws DataNotFoundException {

        List<StatusOrder> statusOrder = statusOrderRepository.findByStatus(Status);

        if (statusOrder.isEmpty()) {
            throw new DataNotFoundException(
                "Cannot find statusOrder for Status (You need write correctly!!!): " + Status);
        }

        return statusOrder;
    }

}
