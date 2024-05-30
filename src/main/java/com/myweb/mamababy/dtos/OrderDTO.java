package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class OrderDTO {

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("store_id")
    private int storeId;

    @JsonProperty("voucher_id")
    private int voucherId;

    @JsonProperty("total_point")
    private int totalPoint;

    @JsonProperty("amount")
    private float amount;

    @JsonProperty("total_discount")
    private float totalDiscount;

    @Min(value = 0, message = "Final money must be >= 0")
    @JsonProperty("final_amount")
    private float finalAmount;

    @NotBlank(message = "shipping_address is required")
    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("order_date")
    private LocalDate orderDate;

    @JsonProperty("type")
    private String type;

}
