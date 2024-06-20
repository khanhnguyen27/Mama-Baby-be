package com.myweb.mamababy.services.ExchangeDetail;

import com.myweb.mamababy.dtos.ExchangeDetailDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Exchange;
import com.myweb.mamababy.models.ExchangeDetail;
import com.myweb.mamababy.models.OrderDetail;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.repositories.ExchangeDetailRepository;
import com.myweb.mamababy.repositories.ExchangeRepository;
import com.myweb.mamababy.repositories.OrderDetailRepository;
import com.myweb.mamababy.repositories.ProductRepository;
import com.myweb.mamababy.responses.exchange.ExchangeDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeDetailService implements IExchangeDetailService{

    private final OrderDetailRepository orderDetailRepository;
    private final ExchangeRepository exchangeRepository;
    private final ExchangeDetailRepository exchangeDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public ExchangeDetailResponse createExchangeDetailResponse(ExchangeDetailDTO newExchangeDetail) throws Exception {
        Product existingProduct = productRepository.findById(newExchangeDetail.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id : " + newExchangeDetail.getProductId()));
        Exchange existingExchange = exchangeRepository.findById(newExchangeDetail.getExchangeId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find exchange with id : " + newExchangeDetail.getExchangeId()));

        ExchangeDetail exchangeDetail = ExchangeDetail.builder()
                .product(existingProduct)
                .exchange(existingExchange)
                .quantity(newExchangeDetail.getQuantity())
                .build();

        exchangeDetailRepository.save(exchangeDetail);
        return ExchangeDetailResponse.fromExchangeDetail(exchangeDetail);
    }

    @Override
    public ExchangeDetailResponse getExchangeDetail(int id) throws DataNotFoundException {
        ExchangeDetail exchangeDetail = exchangeDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find exchange detail with id: " + id));
        return ExchangeDetailResponse.fromExchangeDetail(exchangeDetail);
    }

    @Override
    public ExchangeDetailResponse updateExchangeDetail(int id, ExchangeDetailDTO newExchangeDetail) throws DataNotFoundException {

        ExchangeDetail existingExchangeDetail = exchangeDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find exchange detail with id : " + id));

        Product existingProduct = productRepository.findById(newExchangeDetail.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id : " + newExchangeDetail.getProductId()));

        Exchange existingExchange = exchangeRepository.findById(newExchangeDetail.getExchangeId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find exchange with id : " + newExchangeDetail.getExchangeId()));

        existingExchangeDetail.setProduct(existingProduct);
        existingExchangeDetail.setExchange(existingExchange);
        existingExchangeDetail.setQuantity(newExchangeDetail.getQuantity());

        exchangeDetailRepository.save(existingExchangeDetail);

        return ExchangeDetailResponse.fromExchangeDetail(existingExchangeDetail);
    }

    @Override
    public List<ExchangeDetailResponse> getAllExchangeDetail() throws Exception {
        List<ExchangeDetail> exchangeDetailList = exchangeDetailRepository.findAll();

        if (exchangeDetailList.isEmpty()) {
            throw new DataNotFoundException("Cannot find exchange detail");
        }

        List<ExchangeDetailResponse> exchangeDetailResponses = new ArrayList<>();
        for(ExchangeDetail exchangeDetail :exchangeDetailList){
            exchangeDetailResponses.add(ExchangeDetailResponse.fromExchangeDetail(exchangeDetail));
        }
        return exchangeDetailResponses;
    }

    @Override
    public ExchangeDetailResponse deleteExchangeDetail(int id) throws DataNotFoundException {

        ExchangeDetail existingExchangeDetail = exchangeDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find exchange detail with id : " + id));

        exchangeDetailRepository.deleteById(id);
        return ExchangeDetailResponse.fromExchangeDetail(existingExchangeDetail);
    }

    @Override
    public List<ExchangeDetailResponse> findByExchangeId(int exchangeId) throws DataNotFoundException {

        List<ExchangeDetail> exchangeDetails = exchangeDetailRepository.findByExchangeId(exchangeId);

        if (exchangeDetails.isEmpty()) {
            throw new DataNotFoundException("Cannot find exchange detail for exchange with id: " + exchangeId);
        }

        List<ExchangeDetailResponse> exchangeDetailResponses = new ArrayList<>();

        for(ExchangeDetail exchangeDetail :exchangeDetails){
            exchangeDetailResponses.add(ExchangeDetailResponse.fromExchangeDetail(exchangeDetail));
        }

        return exchangeDetailResponses;
    }
}
