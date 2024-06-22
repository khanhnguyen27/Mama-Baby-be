package com.myweb.mamababy.services.Refund;

import com.myweb.mamababy.dtos.RefundDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Refund;
import com.myweb.mamababy.responses.refunds.RefundResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IRefundService {

    List<Refund> findByYear(int year);

    Refund createRefund(RefundDTO refundDTO) throws Exception;

    Refund getRefundById(int id) throws DataNotFoundException;

    Page<RefundResponse> getAllRefund(String status, PageRequest pageRequest);

    Refund updateRefund(int id, RefundDTO refundDTO) throws DataNotFoundException;

    List<Refund> findByUserId(int userId) throws DataNotFoundException;

    List<Refund> findByStoreId(int storeId) throws DataNotFoundException;
}
