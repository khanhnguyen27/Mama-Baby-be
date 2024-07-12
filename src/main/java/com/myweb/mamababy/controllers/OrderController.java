package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.OrderDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.order.OrderListResponse;
import com.myweb.mamababy.responses.order.OrderResponse;
import com.myweb.mamababy.services.Order.IOrderService;
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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    // Create Order
    @PostMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Order newOrders = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                .message("Create New Orders Successfully!!!").status(HttpStatus.CREATED)
                .data(OrderResponse.fromOrder(newOrders))
                .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Order Find By ID
    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getOrderById(@Valid @PathVariable("id") int orderId) {
        try {
            Order existingOrder = orderService.getOrder(orderId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Order With OrderId = " + orderId + " Found Successfully!!!")
                    .status(HttpStatus.OK)
                    .data(OrderResponse.fromOrder(existingOrder))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Order Find By UserId
    @GetMapping("/user/{user_id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable("user_id") int userId) {
        try {
            List<Order> orders = orderService.findByUserId(userId);
            List<OrderResponse> orderResponses = orders.stream()
                    .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                    .map(OrderResponse::fromOrder)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Order With UserId = " + userId + " Found Successfully!!!")
                    .data(orderResponses)
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Order Find By StoreId
    @GetMapping("/store/{store_id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getOrdersByStoreId(@Valid @PathVariable("store_id") int storeId) {
        try {
            List<Order> orders = orderService.findByStoreId(storeId);
            List<OrderResponse> orderResponses = orders.stream()
                    .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                    .map(OrderResponse::fromOrder)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Order With StoreId = " + storeId + " Found Successfully!!!")
                    .data(orderResponses)
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Order Find By Shipping Address and Order Date
    @GetMapping("/Keywords_by_orderDate_and_shippingAddress")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getOrdersByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) throws DataNotFoundException {

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());

        Page<OrderResponse> orderPage = orderService
                .getOrdersByKeyword(keyword, pageRequest)
                .map(OrderResponse::fromOrder);

        int totalPages = orderPage.getTotalPages();
        List<OrderResponse> orders = orderPage.getContent();

        OrderListResponse orderListResponse = OrderListResponse
                .builder()
                .orders(orders)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get Orders Successfully!!!!")
                .data(orderListResponse)
                .status(HttpStatus.OK)
                .build());
    }

    // Get All Order
    @GetMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getAllOrder() throws Exception {
        List<Order> orders = orderService.getAllOrder();

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get All Orders Successfully!!!")
                        .data(orders.stream()
                                .map(OrderResponse::fromOrder)
                                .collect(Collectors.toList()))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    // Update Order
    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> updateOrder(
        @Valid @PathVariable int id,
        @Valid @RequestBody OrderDTO orderDTO,
        BindingResult result
    ){
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            Order updatedOrders = orderService.updateOrder(id, orderDTO);

            return ResponseEntity.ok(ResponseObject.builder()
                .message("Update Orders Successfully!!!")
                .status(HttpStatus.OK)
                .data(OrderResponse.fromOrder(updatedOrders))
                .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get By Year
    @GetMapping("/findByYear")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getOrdersByYear(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) int year) {
        try {
            List<Order> orders = orderService.findByYear(year);
            return ResponseEntity.ok(ResponseObject.builder()
                .message("Orders for year " + year + " found successfully!!!")
                .data(orders.stream()
                    .map(OrderResponse::fromOrder)
                    .collect(Collectors.toList()))
                .status(HttpStatus.OK)
                .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
