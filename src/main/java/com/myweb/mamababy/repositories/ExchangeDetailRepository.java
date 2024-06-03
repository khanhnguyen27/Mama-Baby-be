package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.ExchangeDetail;
import com.myweb.mamababy.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeDetailRepository extends JpaRepository<ExchangeDetail, Integer> {
    List<ExchangeDetail> findByExchangeId(int exchangeId);
}
