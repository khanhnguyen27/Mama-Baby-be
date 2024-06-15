package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.RefundDTO;
import com.myweb.mamababy.models.Refund;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.refunds.RefundResponse;
import com.myweb.mamababy.services.Refund.IRefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final IRefundService refundService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("")
    public ResponseEntity<?> createExchange(
            @Valid @RequestBody RefundDTO refundDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Refund newRefund = refundService.createRefund(refundDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Create new exchange request successfully")
                    .status(HttpStatus.CREATED)
                    .data(RefundResponse.fromRefund(newRefund))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getAllOrder() throws Exception {
        List<RefundResponse> refundResponseList = refundService.getAllRefund();
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get all refund successfully!!!")
                        .data(refundResponseList)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getRefundById(@Valid @PathVariable("id") int refundId) {
        try {
            RefundResponse existingRefund = refundService.getRefund(refundId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Refund with refund id = " + refundId + " Found Successfully!!!")
                    .status(HttpStatus.OK)
                    .data(existingRefund)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{user_id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable("user_id") int userId) {
        try {
            List<RefundResponse> refundResponseList = refundService.findByUserId(userId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Refund ưith user Id = " + userId + " Found Successfully!!!")
                    .data(refundResponseList)
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
