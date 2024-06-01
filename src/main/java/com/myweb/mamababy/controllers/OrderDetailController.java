package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.OrderDetailDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.OrderDetail;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.order.OrderDetailResponse;
import com.myweb.mamababy.services.OrderDetails.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    // Create OrderDetail
    @PostMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
//  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Create New OrderDetail Successfully!!!")
                    .status(HttpStatus.CREATED)
                    .data(OrderDetailResponse.fromOrderDetail(newOrderDetail))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // Get Order Detail By Id
    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable("id") int id) throws DataNotFoundException {
        try {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("OrderDetail With OrderDetailId = " + id + " Found Successfully!!!")
                .status(HttpStatus.OK)
                .data(OrderDetailResponse.fromOrderDetail(orderDetail))
                .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
    }
    }

    // Get Order Detail By OrderId
    @GetMapping("/order/{orderId}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getByOrderId(
            @Valid @PathVariable("orderId") int orderId
    ) {
        try {
            List<OrderDetail> orders = orderDetailService.findByOrderId(orderId);
            List<OrderDetailResponse> orderDetailResponses = orders.stream()
                    .map(OrderDetailResponse::fromOrderDetail)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("OrderDetail With OrderId = " + orderId + " Found Successfully!!!")
                    .data(orderDetailResponses)
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get All OrderDetail
    @GetMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getAllOrderDetail() throws Exception {
        List<OrderDetail> orders = orderDetailService.getAllOrderDetail();
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get All OrderDetails Successfully!!!")
                        .data(orders.stream()
                                .map(OrderDetailResponse::fromOrderDetail)
                                .collect(Collectors.toList()))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    // Update Order Detail
    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
//    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") int id,
            @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("OrderDetail With OrderDetailId = " + id + " Updated Successfully!!!")
                    .status(HttpStatus.OK)
                    .data(OrderDetailResponse.fromOrderDetail(orderDetail))
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete Order Detail
//    @DeleteMapping("/{id}")
//    @CrossOrigin(origins = "http://localhost:3000")
////    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
////    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//    public ResponseEntity<?> deleteOrderDetail(
//            @Valid @PathVariable("id") int id) {  try {
//
//        orderDetailService.deleteOrderDetail(id);
//        return ResponseEntity.ok(ResponseObject.builder()
//                .data(null)
//                .message(String.format("OrderDetail With Id = %d Deleted Successfully!!!", id))
//                .status(HttpStatus.OK)
//                .build());
//
//    }catch (Exception e){
//        return ResponseEntity.badRequest().body(e.getMessage());
//    }
//    }
    }

