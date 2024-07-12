package com.myweb.mamababy.services.Exchange;

import com.myweb.mamababy.dtos.ExchangeDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Exchange;
import com.myweb.mamababy.responses.exchange.ExchangeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.List;

public interface IExchangeService {

    Exchange createExchange(ExchangeDTO exchangeDTO) throws Exception;

    Exchange getExchangeById(int id) throws Exception;

    Page<ExchangeResponse> getAllExchange(String status, PageRequest pageRequest);

    Exchange updateExchange(int id, ExchangeDTO exchangeDTO) throws Exception;

    List<ExchangeResponse> findByUserId(int userId) throws DataNotFoundException;

    List<ExchangeResponse> findByStoreId(int storeId) throws DataNotFoundException;

    void deleteExchange(int id) throws IOException;

}
