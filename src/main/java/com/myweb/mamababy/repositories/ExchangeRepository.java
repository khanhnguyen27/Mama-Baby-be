package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Exchange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {

    List<Exchange> findByUserId(int userId);

    List<Exchange> findByStoreId(int storeId);

    @Query("SELECT e FROM Exchange e WHERE " +
            "(:status IS NULL OR :status = '' OR e.status LIKE %:status%)")
    Page<Exchange> searchExchange
            (@Param("status") String status,
             Pageable pageable);
}
