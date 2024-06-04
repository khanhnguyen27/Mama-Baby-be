package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.ExchangeDTO;
import com.myweb.mamababy.dtos.ExchangeDetailDTO;
import com.myweb.mamababy.dtos.OrderDetailDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Exchange;
import com.myweb.mamababy.models.ExchangeDetail;
import com.myweb.mamababy.models.OrderDetail;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.exchange.ExchangeDetailResponse;
import com.myweb.mamababy.responses.order.OrderDetailResponse;
import com.myweb.mamababy.services.ExchangeDetail.IExchangeDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/exchange_details")
@RequiredArgsConstructor
public class ExchangeDetailController {

    private final IExchangeDetailService exchangeDetailService;

    @PostMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
//  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> creatExchangeDetail(
            @Valid @RequestBody ExchangeDetailDTO exchangeDetailDTO) {
        try {
            ExchangeDetailResponse newExchange = exchangeDetailService.createExchangeDetailResponse(exchangeDetailDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Create New OrderDetail Successfully!!!")
                    .status(HttpStatus.CREATED)
                    .data(newExchange)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getAllExchangeDetail(){
        try{
            List<ExchangeDetailResponse> exchangeDetailResponses = exchangeDetailService.getAllExchangeDetail();
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Get All OrderDetails Successfully!!!")
                            .data(exchangeDetailResponses)
                            .status(HttpStatus.OK)
                            .build());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getExchangeDetail(
            @Valid @PathVariable("id") int id) throws DataNotFoundException {
        try {
            ExchangeDetailResponse exchangeDetailResponse = exchangeDetailService.getExchangeDetail(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("OrderDetail With OrderDetailId = " + id + " Found Successfully!!!")
                    .status(HttpStatus.OK)
                    .data(exchangeDetailResponse)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/exchange/{exchange_id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getByExchangeId(
            @Valid @PathVariable("exchange_id") int exchangeId
    ) {
        try {
            List<ExchangeDetailResponse> exchangeDetailResponses = exchangeDetailService.findByExchangeId(exchangeId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Exchange detail with exchange id = " + exchangeId + " Found Successfully!!!")
                    .data(exchangeDetailResponses)
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
//    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateExchangeDetail(
            @Valid @PathVariable("id") int id,
            @RequestBody ExchangeDetailDTO exchangeDetailDTO) {
        try {
            ExchangeDetailResponse exchangeDetailResponse = exchangeDetailService.updateExchangeDetail(id, exchangeDetailDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("OrderDetail With OrderDetailId = " + id + " Updated Successfully!!!")
                    .status(HttpStatus.OK)
                    .data(exchangeDetailResponse)
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
