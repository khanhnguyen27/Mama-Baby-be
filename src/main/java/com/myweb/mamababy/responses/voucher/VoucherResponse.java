package com.myweb.mamababy.responses.voucher;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Voucher;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class VoucherResponse {

    private int id;

    @JsonProperty("store_id")
    private int storeId;

    private String code;
    private int discountValue;
    private String description;
    private LocalDate endAt;
    private boolean isActive;

    public static VoucherResponse fromVoucher(Voucher voucher) {
        return VoucherResponse.builder()
                .id(voucher.getId())
                .storeId(voucher.getStore().getId())
                .code(voucher.getCode())
                .discountValue(voucher.getDiscountValue())
                .description(voucher.getDescription())
                .endAt(voucher.getEndAt())
                .isActive(voucher.isActive())
                .build();
    }
}
