package com.myweb.mamababy.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDTO {

    @Min(value = 5000, message = "Price must be greater than or equal to 0")
    @Max(value = 10000000, message = "Price must be less than or equal to 10,000,000")
    private int finalAmount;

    @NotEmpty(message = "Bank code is required")
    private String bankCode;

    private List<PaymentOrderDTO> orders;
}
