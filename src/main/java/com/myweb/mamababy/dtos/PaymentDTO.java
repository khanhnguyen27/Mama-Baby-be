package com.myweb.mamababy.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDTO {

    @Min(value = 5000, message = "Price must be greater than or equal to 0")
    private float finalAmount;

    @NotEmpty(message = "Bank code is required")
    private String bankCode;

    private int orderId;

//    private int packageId;

    private int storeId;
}
