package com.myweb.mamababy.services.RefundDetails;

import com.myweb.mamababy.dtos.RefundDetailDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.ProductRepository;
import com.myweb.mamababy.repositories.RefundDetailRepository;
import com.myweb.mamababy.repositories.RefundRepository;
import com.myweb.mamababy.responses.exchange.ExchangeDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundDetailService implements IRefundDetailService{

    private final ProductRepository productRepository;
    private final RefundDetailRepository refundDetailRepository;
    private final RefundRepository refundRepository;

    @Override
    public RefundDetail createRefundDetailExchange(RefundDetailDTO newRefundDetailDTO) throws Exception {
        Product existingProduct = productRepository.findById(newRefundDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id : " + newRefundDetailDTO.getProductId()));
        Refund existingRefund = refundRepository.findById(newRefundDetailDTO.getRefundId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find exchange with id : " + newRefundDetailDTO.getRefundId()));

        return RefundDetail.builder()
                .product(existingProduct)
                .refund(existingRefund)
                .unitPrice(newRefundDetailDTO.getUnitPrice())
                .quantity(newRefundDetailDTO.getQuantity())
                .amount(newRefundDetailDTO.getAmount())
                .build();

    }

    @Override
    public RefundDetail getRefundDetail(int id) throws DataNotFoundException {
        return refundDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find exchange detail with id: " + id));
    }

    @Override
    public RefundDetail updateRefundDetail(int id, RefundDetailDTO refundDetailDTO) throws DataNotFoundException {

        RefundDetail existingRefundDetail = refundDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find exchange detail with id : " + id));

        Product existingProduct = productRepository.findById(refundDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id : " + refundDetailDTO.getProductId()));

        Refund existingRefund = refundRepository.findById(refundDetailDTO.getRefundId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find exchange with id : " + refundDetailDTO.getRefundId()));

        existingRefundDetail.setProduct(existingProduct);
        existingRefundDetail.setRefund(existingRefund);
        existingRefundDetail.setQuantity(refundDetailDTO.getQuantity());
        existingRefundDetail.setUnitPrice(refundDetailDTO.getUnitPrice());
        existingRefundDetail.setAmount(refundDetailDTO.getAmount());

        return refundDetailRepository.save(existingRefundDetail);
    }

    @Override
    public List<RefundDetail> getAllRefundDetail() throws Exception {
        List<RefundDetail> refundDetails = refundDetailRepository.findAll();

        if (refundDetails.isEmpty()) {
            throw new DataNotFoundException("Cannot find refund detail");
        }

        return refundDetails;
    }

    @Override
    public RefundDetail deleteRefundDetail(int id) throws DataNotFoundException {
        RefundDetail existingRefundDetail = refundDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find exchange detail with id : " + id));

        refundDetailRepository.deleteById(id);
        return existingRefundDetail;
    }

    @Override
    public List<RefundDetail> findByRefundId(int refundId) throws DataNotFoundException {
        List<RefundDetail> refundDetails = refundDetailRepository.findByRefundId(refundId);
        if (refundDetails.isEmpty()) {
            throw new DataNotFoundException("Cannot find exchange detail for exchange with id: " + refundId);
        }
        return refundDetails;
    }
}
