package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class OrderDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("voucher_id")
    private int voucher_id;

    @JsonProperty("total_point")
    private int total_point;

    @JsonProperty("amount")
    private float amount;

    @JsonProperty("total_discount")
    private float total_discount;

    @Min(value = 0, message = "Final money must be >= 0")
    @JsonProperty("final_amount")
    private float final_amount;

    @NotBlank(message = "shipping_address is required")
    @JsonProperty("shipping_address")
    private String shipping_address;

    @JsonProperty("payment_method")
    private String payment_method;

    @JsonProperty("type")
    private String type;

    @JsonProperty("oder_date")
    private Date oder_date;
}
