package com.myweb.mamababy.responses.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Order;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private int id;

    @JsonProperty("user_id")
    private int userId;

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

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("order_date")
    private LocalDate orderDate;

    @JsonProperty("type")
    private String type;

    public static OrderResponse fromOrder(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .voucherId(order.getVoucher().getId())
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
