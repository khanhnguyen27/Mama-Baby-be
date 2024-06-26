package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class VoucherDTO {

    @JsonProperty("code")
    private String code;

    @JsonProperty("discount_value")
    private int discountValue;

    @NotBlank(message = "description is required!")
    @JsonProperty("description")
    private String description;

    @JsonProperty("end_at")
    private LocalDate endAt;

    @NotNull(message = "Store ID is required.")
    @JsonProperty("store_id")
    private int storeId;

    @JsonProperty("is_active")
    private boolean isActive;
}

