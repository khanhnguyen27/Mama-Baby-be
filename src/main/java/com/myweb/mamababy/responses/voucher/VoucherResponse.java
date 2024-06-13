package com.myweb.mamababy.responses.voucher;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Voucher;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class VoucherResponse {

    private int id;

    private String code;

    @JsonProperty("discount_value")
    private int discountValue;

    private String description;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate endAt;

    private boolean isActive;

    public static VoucherResponse fromVoucher(Voucher voucher) {
        return VoucherResponse.builder()
                .id(voucher.getId())
                .code(voucher.getCode())
                .discountValue(voucher.getDiscountValue())
                .description(voucher.getDescription())
                .endAt(voucher.getEndAt())
                .isActive(voucher.isActive())
                .build();
    }
}
