package com.myweb.mamababy.responses.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.OrderDetail;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderDetailResponse {

    private int id;

    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("unit_price")
    private float unitPrice;

    @JsonProperty("unit_point")
    private int unitPoint;

    @JsonProperty("amount_price")
    private float amountPrice;

    @JsonProperty("amount_point")
    private int amountPoint;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail) {
        return OrderDetailResponse
                .builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getProduct().getId())
                .quantity(orderDetail.getQuantity())
                .unitPrice(orderDetail.getUnitPrice())
                .unitPoint(orderDetail.getUnitPoint())
                .amountPrice(orderDetail.getAmountPrice())
                .amountPoint(orderDetail.getAmountPoint())
                .build();
    }
}
