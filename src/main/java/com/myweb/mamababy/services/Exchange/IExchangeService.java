package com.myweb.mamababy.services.Exchange;

import com.myweb.mamababy.dtos.ExchangeDTO;
import com.myweb.mamababy.dtos.ProductDTO;
import com.myweb.mamababy.models.Exchange;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.responses.exchange.ExchangeResponse;
import com.myweb.mamababy.responses.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IExchangeService {

    Exchange createExchange(ExchangeDTO exchangeDTO, MultipartFile file) throws Exception;

    Exchange getExchangeById(int id) throws Exception;

    Page<ExchangeResponse> getAllExchange(PageRequest pageRequest);

    Exchange updateExchange(int id, ExchangeDTO exchangeDTO, MultipartFile file) throws Exception;

    void deleteExchange(int id) throws IOException;

    Boolean checkFileImage(MultipartFile file);

    String storeFile(MultipartFile file) throws IOException;

    void deleteFile(String filename) throws IOException;
}
