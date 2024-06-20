package com.myweb.mamababy.services.RefundDetails;

import com.myweb.mamababy.dtos.RefundDetailDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.RefundDetail;

import java.util.List;

public interface IRefundDetailService {

    RefundDetail createRefundDetailExchange(RefundDetailDTO newRefundDetailDTO) throws Exception;

    RefundDetail getRefundDetail(int id) throws DataNotFoundException;

    RefundDetail updateRefundDetail(int id, RefundDetailDTO refundDetailDTO)
            throws DataNotFoundException;

    List<RefundDetail> getAllRefundDetail() throws Exception;

    RefundDetail deleteRefundDetail(int id) throws DataNotFoundException;

    List<RefundDetail> findByRefundId(int refundId) throws DataNotFoundException;
}
