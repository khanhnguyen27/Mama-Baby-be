package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
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

    @JsonProperty("amount")
    private float amount;

    @JsonProperty("status")
    private String status;

    @NotNull(message = "Store ID is required.")
    @JsonProperty("store_id")
    private int storeId;

    @NotNull(message = "User ID is required.")
    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("cart_items_exchange")
    private List<CartItemExchangeDTO> cartItemExchange;

}
