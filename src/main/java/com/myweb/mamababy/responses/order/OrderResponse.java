package com.myweb.mamababy.responses.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.dtos.CartItemDTO;
import com.myweb.mamababy.models.Order;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OrderResponse {

    private int id;


    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String shippingAddress;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate orderDate;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("store_id")
    private int storeId;

    @JsonProperty("voucher_id")
    private int voucherId;

    @JsonProperty("total_point")
    private int totalPoint;

    @JsonProperty("amount")
    private float amount;

    @JsonProperty("total_discount")
    private float totalDiscount;

    @JsonProperty("final_amount")
    private float finalAmount;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("type")
    private String type;

    @JsonProperty("order_detail_list")
    private List<OrderDetailResponse> orderDetailResponses;

    @JsonProperty("status_order_list")
    private List<StatusOrderResponse> statusOrderResponses;

    public static OrderResponse fromOrder(Order order) {
        return OrderResponse.builder()
            .id(order.getId())
            .userId(order.getUser().getId())
            .voucherId(order.getVoucher() != null ? order.getVoucher().getId() : 0)
            .storeId(order.getStore().getId())
            .totalPoint(order.getTotalPoint())
            .amount(order.getAmount())
            .totalDiscount(order.getTotalDiscount())
            .finalAmount(order.getFinalAmount())
            .fullName(order.getFullName())
            .phoneNumber(order.getPhoneNumber())
            .shippingAddress(order.getShippingAddress())
            .paymentMethod(order.getPaymentMethod())
            .orderDate(order.getOrderDate())
            .type(order.getType())
            .orderDetailResponses(order.getOrderDetails().stream().map(OrderDetailResponse::fromOrderDetail).toList())
            .statusOrderResponses(order.getStatusOrders().stream().map(StatusOrderResponse::fromStatusOrder).toList())
            .build();
    }
}
