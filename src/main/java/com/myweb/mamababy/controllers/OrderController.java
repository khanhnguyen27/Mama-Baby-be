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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    // Create Order
    @PostMapping("")
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
            Order newOrder = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Create New Order Successfully!!!")
                    .status(HttpStatus.CREATED)
                    .data(OrderResponse.fromOrder(newOrder))
                    .build());
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
}

    //Order Find By ID
    @GetMapping("/{id}")
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
    public ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable("user_id") int userId) {
        try {
            List<Order> orders = orderService.findByUserId(userId);
            List<OrderResponse> orderResponses = orders.stream()
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
    public ResponseEntity<?> getOrdersByStoreId(@Valid @PathVariable("store_id") int storeId) {
        try {
            List<Order> orders = orderService.findByStoreId(storeId);
            List<OrderResponse> orderResponses = orders.stream()
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

    // Order Find By Type, Shipping Adress and Order Date
    @GetMapping("/get-orders-by-keyword")
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
//  @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable int id,
            @Valid @RequestBody OrderDTO orderDTO) {

        try {
            Order order = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Order With OrderId = " + id + " Updated Successfully!!!")
                    .status(HttpStatus.OK)
                    .data(OrderResponse.fromOrder(order))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete Order
//    @DeleteMapping("/{id}")
////  @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public  ResponseEntity<?> deleteVoucher(@Valid @PathVariable int id){
//        try {
//
//            orderService.deleteOrder(id);
//            return ResponseEntity.ok(ResponseObject.builder()
//                    .data(null)
//                    .message(String.format("Order With Id = %d Deleted Successfully!!!", id))
//                    .status(HttpStatus.OK)
//                    .build());
//
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
}