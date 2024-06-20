package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.ExchangeDetailDTO;
import com.myweb.mamababy.dtos.RefundDetailDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.RefundDetail;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.exchange.ExchangeDetailResponse;
import com.myweb.mamababy.responses.refunds.RefundDetailResponse;
import com.myweb.mamababy.services.RefundDetails.IRefundDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/refund_details")
@RequiredArgsConstructor
public class RefundDetailController {

    private final IRefundDetailService refundDetailService;

    @PostMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> creatRefundDetail(
            @Valid @RequestBody RefundDetailDTO refundDetailDTO) {
        try {
            RefundDetail newRefundDetail = refundDetailService.createRefundDetailExchange(refundDetailDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Create New OrderDetail Successfully!!!")
                    .status(HttpStatus.CREATED)
                    .data(newRefundDetail)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getAllRefundDetail(){
        try{
            List<RefundDetail> refundDetails = refundDetailService.getAllRefundDetail();
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Get All OrderDetails Successfully!!!")
                            .data(refundDetails.stream().map(RefundDetailResponse::fromRefundDetail).toList())
                            .status(HttpStatus.OK)
                            .build());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getRefundDetail(
            @Valid @PathVariable("id") int id) throws DataNotFoundException {
        try {
            RefundDetail refundDetail = refundDetailService.getRefundDetail(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("OrderDetail With OrderDetailId = " + id + " Found Successfully!!!")
                    .status(HttpStatus.OK)
                    .data(RefundDetailResponse.fromRefundDetail(refundDetail))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/refund/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getByRefundId(
            @Valid @PathVariable("id") int refundId
    ) {
        try {
            List<RefundDetail> refundDetails = refundDetailService.findByRefundId(refundId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Exchange detail with exchange id = " + refundId + " Found Successfully!!!")
                    .data(refundDetails.stream().map(RefundDetailResponse::fromRefundDetail).toList())
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> updateExchangeDetail(
            @Valid @PathVariable("id") int id,
            @RequestBody RefundDetailDTO refundDetailDTO) {
        try {
            RefundDetail refundDetail = refundDetailService.updateRefundDetail(id, refundDetailDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("OrderDetail With OrderDetailId = " + id + " Updated Successfully!!!")
                    .status(HttpStatus.OK)
                    .data(RefundDetailResponse.fromRefundDetail(refundDetail))
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
