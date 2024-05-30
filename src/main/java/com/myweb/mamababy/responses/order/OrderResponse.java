package com.myweb.mamababy.responses.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Order;
import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OrderResponse {

    private int id;
    private String shippingAddress;
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

    public static OrderResponse fromOrder(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .voucherId(order.getVoucher().getId())
                .storeId(order.getStore().getId())
                .totalPoint(order.getTotalPoint())
                .amount(order.getAmount())
                .totalDiscount(order.getTotalDiscount())
                .finalAmount(order.getFinalAmount())
                .shippingAddress(order.getShippingAddress())
                .paymentMethod(order.getPaymentMethod())
                .orderDate(order.getOrderDate())
                .type(order.getType())
                .build();
    }
}
