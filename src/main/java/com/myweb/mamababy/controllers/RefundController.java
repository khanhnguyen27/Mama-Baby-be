package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.RefundDTO;
import com.myweb.mamababy.models.Refund;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.refunds.RefundListResponse;
import com.myweb.mamababy.responses.refunds.RefundResponse;
import com.myweb.mamababy.services.Refund.IRefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final IRefundService refundService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("")
    public ResponseEntity<?> createRefund(
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
    public ResponseEntity<?> getAllRefund(
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "12", name = "limit") int limit
    ){
        int totalPages = 0;
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                //Sort.by("createdAt").descending()
                Sort.by("id").ascending()
        );
        Page<RefundResponse> refundPage = refundService
                .getAllRefund(status, pageRequest);
        totalPages = refundPage.getTotalPages();
        List<RefundResponse> refunds = refundPage.getContent();

        RefundListResponse refundListResponse = RefundListResponse
                .builder()
                .refunds(refunds)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get exchange request successfully")
                .data(refundListResponse)
                .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getRefundById(
            @Valid @PathVariable("id") int refundId) {
        try {
            Refund existingRefund = refundService.getRefundById(refundId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Refund with refund id = " + refundId + " Found Successfully!!!")
                    .status(HttpStatus.OK)
                    .data(RefundResponse.fromRefund(existingRefund))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getRefundByUserId(
            @Valid @PathVariable("id") int userId) {
        try {
            List<Refund> refundResponseList = refundService.findByUserId(userId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Refund ưith user Id = " + userId + " Found Successfully!!!")
                    .data(refundResponseList.stream().map(RefundResponse::fromRefund).toList())
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/store/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getRefundByStoreId(
            @Valid @PathVariable("id") int storeId) {
        try{
            List<Refund> refundResponseList = refundService.findByStoreId(storeId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Exchange with store id = " + storeId + " Found Successfully!!!")
                    .data(refundResponseList.stream().map(RefundResponse::fromRefund).toList())
                    .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRefund(
            @PathVariable int id,
            @Valid @RequestBody RefundDTO refundDTO
    ) {
        try {
            Refund updateRefund = refundService.updateRefund(id, refundDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(RefundResponse.fromRefund(updateRefund))
                    .message("Update product successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get By Year
    @GetMapping("/findByYear")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getRefundByYear(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) int year) {
        try {
            List<Refund> refunds = refundService.findByYear(year);
            return ResponseEntity.ok(ResponseObject.builder()
                .message("Refunds for year " + year + " found successfully!!!")
                .data(refunds.stream()
                    .map(RefundResponse::fromRefund)
                    .collect(Collectors.toList()))
                .status(HttpStatus.OK)
                .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
