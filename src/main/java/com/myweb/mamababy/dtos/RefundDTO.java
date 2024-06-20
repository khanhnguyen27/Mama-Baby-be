package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefundDTO {

    @NotEmpty(message = "Description is required.")
    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

    @JsonProperty("amount")
    private float amount;

    @NotNull(message = "Store ID is required.")
    @JsonProperty("store_id")
    private int storeId;

    @NotNull(message = "User ID is required.")
    @JsonProperty("user_id")
    private int userId;

    @NotNull(message = "Order ID is required.")
    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("cart_items_refund")
    private List<CartItemRefundDTO> cartItemRefund;
}
