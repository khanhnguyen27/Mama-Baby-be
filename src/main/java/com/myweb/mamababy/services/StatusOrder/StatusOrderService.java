package com.myweb.mamababy.services.StatusOrder;

import com.myweb.mamababy.dtos.StatusOrderDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.models.StatusOrder;
import com.myweb.mamababy.repositories.OrderRepository;
import com.myweb.mamababy.repositories.StatusOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusOrderService implements IStatusOrderService {

    private final OrderRepository orderRepository;
    private final StatusOrderRepository statusOrderRepository;


    @Override
    @Transactional
    public StatusOrder createStatusOrder(StatusOrderDTO statusOrderDTO) throws Exception {

        Order existingOrder = orderRepository.findById(statusOrderDTO.getOrderId()).orElseThrow(() ->
                new DataNotFoundException("Cannot find statusOrder with orderId: " + statusOrderDTO.getOrderId()));

        StatusOrder newStatusOrder = StatusOrder.builder()

                .order(existingOrder)
                .status(statusOrderDTO.getStatus())
                .date(statusOrderDTO.getDate())
                .build();

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
        existingStatusOrder.setDate(statusOrderDTO.getDate());

        return statusOrderRepository.save(existingStatusOrder);
    }

    @Override
    public List<StatusOrder> getAllStatusOrder() throws Exception {
        return statusOrderRepository.findAll();
    }
}