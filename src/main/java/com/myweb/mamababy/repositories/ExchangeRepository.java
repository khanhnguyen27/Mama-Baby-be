package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Exchange;
import com.myweb.mamababy.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {
}
