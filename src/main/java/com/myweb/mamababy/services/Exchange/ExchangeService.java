package com.myweb.mamababy.services.Exchange;

import com.myweb.mamababy.dtos.CartItemExchangeDTO;
import com.myweb.mamababy.dtos.ExchangeDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.*;
import com.myweb.mamababy.responses.exchange.ExchangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExchangeService implements IExchangeService{

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ExchangeRepository exchangeRepository;
    private final ExchangeDetailRepository exchangeDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public Exchange createExchange(ExchangeDTO exchangeDTO) throws Exception {

        User existingUser = userRepository
                .findById(exchangeDTO.getUserId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find user with id: "+exchangeDTO.getUserId()));
        Store existingStore = storeRepository
                .findById(exchangeDTO.getStoreId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find store with id: "+exchangeDTO.getStoreId()));
        Order existingOrder = orderRepository
                .findById(exchangeDTO.getOrderId())
                .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find store with id: "+exchangeDTO.getStoreId()));

        if(!existingUser.getIsActive() || !existingStore.isActive()){
            throw new DataNotFoundException("Invalid");
        }
        Exchange newExchange = Exchange.builder()
                .description(exchangeDTO.getDescription())
                .status("PROCESSING")
                .createDate(LocalDateTime.now().plusHours(7))
                .user(existingUser)
                .store(existingStore)
                .order(existingOrder)
                .build();

        List<ExchangeDetail> exchangeDetails = new ArrayList<>();

        for (CartItemExchangeDTO cartItemExchangeDTO : exchangeDTO.getCartItemExchange()){

            // Tạo một đối tượng ExchangeDetail từ CartItemDTO
            ExchangeDetail exchangeDetail = new ExchangeDetail();
            exchangeDetail.setExchange(newExchange);

            int productId = cartItemExchangeDTO.getProductId();
            int quantity = cartItemExchangeDTO.getQuantity();

            Product existingProduct = productRepository
                    .findById(productId)
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find product with id: "+productId));

            exchangeDetail.setProduct(existingProduct);
            exchangeDetail.setQuantity(quantity);

            exchangeDetails.add(exchangeDetail);
        }

        newExchange.setExchangeDetails(exchangeDetails);
        exchangeRepository.save(newExchange);
        exchangeDetailRepository.saveAll(exchangeDetails);
        return newExchange;

    }

    @Override
    public Exchange getExchangeById(int id) throws Exception {
        Optional<Exchange> optionalExchange = exchangeRepository.findById(id);
        if (optionalExchange.isPresent()) {
            return optionalExchange.get();
        }
        throw new DataNotFoundException("Cannot find product with id =" + id);
    }

    @Override
    public Page<ExchangeResponse> getAllExchange(String status ,PageRequest pageRequest) {
        Page<Exchange> exchangePage= exchangeRepository.searchExchange(status, pageRequest);
        return exchangePage.map(ExchangeResponse::fromExchange);
    }

    @Override
    public Exchange updateExchange(int id, ExchangeDTO exchangeDTO) throws Exception {
        Exchange existingExchange = getExchangeById(id);
        if (existingExchange != null) {
            if (exchangeDTO.getStatus() != null &&
                    !exchangeDTO.getStatus().isEmpty())
                existingExchange.setStatus(exchangeDTO.getStatus());

            return exchangeRepository.save(existingExchange);
        }

        return null;
    }

    @Override
    public List<ExchangeResponse> findByUserId(int userId) throws DataNotFoundException {
        List<Exchange> exchanges = exchangeRepository.findByUserId(userId);
        List<ExchangeResponse> exchangeResponses = new ArrayList<>();
        for(Exchange exchange :exchanges){
            exchangeResponses.add(ExchangeResponse.fromExchange(exchange));
        }
        return exchangeResponses;
    }

    @Override
    public List<ExchangeResponse> findByStoreId(int storeId) throws DataNotFoundException {
        List<Exchange> exchanges = exchangeRepository.findByStoreId(storeId);
        List<ExchangeResponse> exchangeResponses = new ArrayList<>();
        for(Exchange exchange :exchanges){
            exchangeResponses.add(ExchangeResponse.fromExchange(exchange));
        }
        return exchangeResponses;
    }

    @Override
    public void deleteExchange(int id) throws IOException {
        Optional<Exchange> optionalExchange = exchangeRepository.findById(id);
        if (optionalExchange.isPresent()) {
            Exchange exchange =optionalExchange.get();
            exchangeRepository.delete(exchange);
        }
    }

}
