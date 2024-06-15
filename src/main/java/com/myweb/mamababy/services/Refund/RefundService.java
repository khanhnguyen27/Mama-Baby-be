package com.myweb.mamababy.services.Refund;

import com.myweb.mamababy.dtos.RefundDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.exceptions.InvalidParamException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.ExchangeRepository;
import com.myweb.mamababy.repositories.OrderRepository;
import com.myweb.mamababy.repositories.RefundRepository;
import com.myweb.mamababy.repositories.UserRepository;
import com.myweb.mamababy.responses.refunds.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class RefundService implements IRefundService{

    private final RefundRepository refundRepository;
    private final UserRepository userRepository;
    private final ExchangeRepository exchangeRepository;
    private final OrderRepository orderRepository;

    @Override
    public Refund createRefund(RefundDTO refundDTO) throws Exception {

        Exchange existingExchange = null;
        Order existingOrder = null;

        User existingUser = userRepository.findById(refundDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find user with id: " + refundDTO.getUserId()));
        if(refundDTO.getExchangeId() == 0 && refundDTO.getOrderId() == 0){
            throw new InvalidParamException("Both exchange id and order id attributes cannot be left blank !");
        }
        if(refundDTO.getExchangeId() != 0){
            existingExchange = exchangeRepository.findById(refundDTO.getExchangeId())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Cannot find exchange with id: " + refundDTO.getExchangeId()));
        }
        if(refundDTO.getOrderId() != 0){
            existingOrder = orderRepository.findById(refundDTO.getOrderId())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Cannot find exchange with id: " + refundDTO.getOrderId()));
        }

        Refund newRefund = Refund.builder()
                .user(existingUser)
                .exchange(existingExchange)
                .order(existingOrder)
                .createDate(LocalDate.now())
                .build();

        return refundRepository.save(newRefund);
    }

    @Override
    public RefundResponse getRefund(int id) throws DataNotFoundException {
        Refund refund =  refundRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find refund with id: " + id));
        return RefundResponse.fromRefund(refund);
    }

    @Override
    public Refund updateRefund(int id, RefundDTO refundDTO) throws DataNotFoundException {
        return null;
    }

    @Override
    public List<RefundResponse> getAllRefund() throws Exception {
        List<Refund> refundList = refundRepository.findAll();
        return refundList.stream().map(RefundResponse::fromRefund).toList();
    }

    @Override
    public List<RefundResponse> findByUserId(int userId) throws DataNotFoundException {
        List<Refund> refundList = refundRepository.findByUserId(userId);
        return refundList.stream().map(RefundResponse::fromRefund).toList();
    }
}
