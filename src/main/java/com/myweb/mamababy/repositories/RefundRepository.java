package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Refund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RefundRepository extends JpaRepository<Refund, Integer> {
    List<Refund> findByUserId(int userId);

    List<Refund> findByStoreId(int storeId);

    @Query("SELECT o FROM Refund o WHERE YEAR(o.createDate) = :year")
    List<Refund> findByRefundDateYear(int year);

    @Query("SELECT r FROM Refund r WHERE " +
            "(:status IS NULL OR :status = '' OR r.status LIKE %:status%)")
    Page<Refund> searchExchange
            (@Param("status") String status,
             Pageable pageable);
}
