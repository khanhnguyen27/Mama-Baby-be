package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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

    @NotBlank(message = "Full name is required.")
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required.")
    private String phoneNumber;

    @JsonProperty("voucher_id")
    private int voucherId;

    @JsonProperty("total_point")
    private int totalPoint;

    @JsonProperty("amount")
    private float amount;

    @JsonProperty("total_discount")
    private float totalDiscount;

    @JsonProperty("final_amount")
    private float finalAmount;

    @NotBlank(message = "shipping_address is required.")
    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("order_date")
    private LocalDate orderDate;

    @JsonProperty("type")
    private String type;

    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItems;

}
