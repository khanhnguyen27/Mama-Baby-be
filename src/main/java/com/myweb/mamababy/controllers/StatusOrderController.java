package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.StatusOrderDTO;
import com.myweb.mamababy.models.StatusOrder;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.order.StatusOrderResponse;
import com.myweb.mamababy.services.StatusOrder.IStatusOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/status_orders")
@RequiredArgsConstructor
public class StatusOrderController {

    private final IStatusOrderService statusOrderService;


    // Create Status Order
    @PostMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> createStatusOrder(
            @Valid @RequestBody StatusOrderDTO statusOrderDTO,
            BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            StatusOrder newStatusOrder = statusOrderService.createStatusOrder((statusOrderDTO));
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Create New StatusOrder Successfully!!!")
                    .status(HttpStatus.CREATED)
                    .data(StatusOrderResponse.fromStatusOrder(newStatusOrder))
                    .build());
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update Status Order
    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
//  @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateStatusOrder(
            @Valid @PathVariable int id,
            @Valid @RequestBody StatusOrderDTO statusOrderDTO) {

        try {
            StatusOrder newStatusOrder = statusOrderService.updateStatusOrder(id, statusOrderDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Order With StatusOrderId = " + id + " Updated Successfully!!!")
                    .status(HttpStatus.OK)
                    .data(StatusOrderResponse.fromStatusOrder(newStatusOrder))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Get All Status Order
    @GetMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getAllStatusOrder() throws Exception {
        List<StatusOrder> statusOrders = statusOrderService.getAllStatusOrder();
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get All StatusOrders Successfully!!!")
                        .data(statusOrders.stream()
                                .map(StatusOrderResponse::fromStatusOrder)
                                .collect(Collectors.toList()))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

}
