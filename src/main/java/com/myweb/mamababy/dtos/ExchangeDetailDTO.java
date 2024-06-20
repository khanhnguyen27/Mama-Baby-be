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

    @NotNull(message = "Product ID is required.")
    @JsonProperty("product_id")
    private int productId;

    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    @JsonProperty("quantity")
    private int quantity;

    @NotNull(message = "Exchange ID is required")
    @JsonProperty("exchange_id")
    private int exchangeId;
}
