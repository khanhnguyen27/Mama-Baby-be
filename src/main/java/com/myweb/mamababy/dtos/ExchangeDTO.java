package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeDTO {

    @NotEmpty(message = "Description is required.")
    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

    @NotNull(message = "Store ID is required.")
    @JsonProperty("store_id")
    private int storeId;

    @NotNull(message = "User ID is required.")
    @JsonProperty("user_id")
    private int userId;

    @NotNull(message = "Order ID is required.")
    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("cart_items_exchange")
    private List<CartItemExchangeDTO> cartItemExchange;

}
