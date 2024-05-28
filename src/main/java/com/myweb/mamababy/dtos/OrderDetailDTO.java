package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetailDTO {

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

}
