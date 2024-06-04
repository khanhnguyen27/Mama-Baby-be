package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExchangeDetailDTO {

    @NotNull(message = "Order detail ID is required.")
    @JsonProperty("order_detail_id")
    private int orderDetailId;

    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    @JsonProperty("quantity")
    private int quantity;

    @NotNull(message = "Exchange ID is required")
    @JsonProperty("exchange_id")
    private int exchangeId;
}
