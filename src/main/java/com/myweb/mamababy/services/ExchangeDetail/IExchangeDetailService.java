package com.myweb.mamababy.services.ExchangeDetail;

import com.myweb.mamababy.dtos.ExchangeDetailDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.responses.exchange.ExchangeDetailResponse;

import java.util.List;

public interface IExchangeDetailService {

    ExchangeDetailResponse createExchangeDetailResponse(ExchangeDetailDTO newExchangeDetail) throws Exception;

    ExchangeDetailResponse getExchangeDetail(int id) throws DataNotFoundException;

    ExchangeDetailResponse updateExchangeDetail(int id, ExchangeDetailDTO newExchangeDetail)
            throws DataNotFoundException;

    List<ExchangeDetailResponse> getAllExchangeDetail() throws Exception;

    ExchangeDetailResponse deleteExchangeDetail(int id) throws DataNotFoundException;

    List<ExchangeDetailResponse> findByExchangeId(int exchangeId) throws DataNotFoundException;

}
