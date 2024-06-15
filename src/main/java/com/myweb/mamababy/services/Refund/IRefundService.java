package com.myweb.mamababy.services.Refund;

import com.myweb.mamababy.dtos.OrderDTO;
import com.myweb.mamababy.dtos.RefundDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.models.Refund;
import com.myweb.mamababy.responses.refunds.RefundResponse;

import java.util.List;

public interface IRefundService {

    Refund createRefund(RefundDTO refundDTO) throws Exception;

    RefundResponse getRefund(int id) throws DataNotFoundException;

    Refund updateRefund(int id, RefundDTO refundDTO) throws DataNotFoundException;

    List<RefundResponse> getAllRefund() throws Exception;

    List<RefundResponse> findByUserId(int userId) throws DataNotFoundException;

}
