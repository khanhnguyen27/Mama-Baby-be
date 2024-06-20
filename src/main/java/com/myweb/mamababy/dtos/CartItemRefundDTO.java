package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemRefundDTO {

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("unit_price")
    private float unitPrice;

    @JsonProperty("amount")
    private int amount ;
}
