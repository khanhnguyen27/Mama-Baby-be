package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.RefundDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefundDetailRepository extends JpaRepository<RefundDetail, Integer> {
    List<RefundDetail> findByRefundId(int refundId);
}
