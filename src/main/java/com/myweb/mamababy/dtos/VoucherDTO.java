package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class VoucherDTO {
    @JsonProperty("store_id")
    private int storeId;

    @JsonProperty("code")
    private String code;

    @JsonProperty("discount_value")
    private int discountValue;

    @NotBlank(message = "description is required")
    @JsonProperty("description")
    private String description;

    @JsonProperty("end_at")
    private LocalDate endAt;
}

