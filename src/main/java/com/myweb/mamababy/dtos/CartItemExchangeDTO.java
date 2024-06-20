package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemExchangeDTO {

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("quantity")
    private int quantity;
}
