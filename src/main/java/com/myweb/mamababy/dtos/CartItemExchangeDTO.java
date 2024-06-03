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

    @JsonProperty("order_detail_id")
    private int orderDetailId;

    @JsonProperty("quantity")
    private int quantity;
}
