package com.myweb.mamababy.responses.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.dtos.CartItemDTO;
import com.myweb.mamababy.models.Order;
import java.time.LocalDate;
import java.util.List;
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

    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItems;

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
            .shippingAddress(order.getShippingAddress())
            .paymentMethod(order.getPaymentMethod())
            .orderDate(order.getOrderDate())
            .type(order.getType())
            .build();
    }
}
