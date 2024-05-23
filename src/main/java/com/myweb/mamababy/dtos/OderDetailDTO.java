package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OderDetailDTO {

    private int id;

    @JsonProperty("oder_id")
    private int oder_id;

    @JsonProperty("product_id")
    private int product_id;

    @JsonProperty("quality")
    private int quality;

    @JsonProperty("unit_price")
    private float unit_price;

    @JsonProperty("unit_point")
    private int unit_point;

    @JsonProperty("amount")
    private float amount;

}
