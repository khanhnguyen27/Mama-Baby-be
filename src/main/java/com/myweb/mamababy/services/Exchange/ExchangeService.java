package com.myweb.mamababy.services.Exchange;

import com.myweb.mamababy.dtos.ExchangeDTO;
import com.myweb.mamababy.models.Exchange;
import com.myweb.mamababy.responses.exchange.ExchangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ExchangeService implements IExchangeService{

    @Override
    public Exchange createExchange(ExchangeDTO exchangeDTO, MultipartFile file) throws Exception {
        return null;
    }

    @Override
    public Exchange getExchangeById(int id) throws Exception {
        return null;
    }

    @Override
    public Page<ExchangeResponse> getAllExchange(PageRequest pageRequest) {
        return null;
    }

    @Override
    public Exchange updateExchange(int id, ExchangeDTO exchangeDTO, MultipartFile file) throws Exception {
        return null;
    }

    @Override
    public void deleteExchange(int id) throws IOException {

    }

    @Override
    public Boolean checkFileImage(MultipartFile file) {
        return null;
    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        return "";
    }

    @Override
    public void deleteFile(String filename) throws IOException {

    }
}
