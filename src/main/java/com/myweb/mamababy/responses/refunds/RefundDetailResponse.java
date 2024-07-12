package com.myweb.mamababy.responses.refunds;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.RefundDetail;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundDetailResponse {

    private int id;

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("unit_price")
    private float unitPrice;

    @JsonProperty("amount")
    private float amount;

    @JsonProperty("refund_id")
    private int refundId;

    public static RefundDetailResponse fromRefundDetail(RefundDetail refundDetail){
        return RefundDetailResponse.builder()
                .id(refundDetail.getId())
                .productId(refundDetail.getProduct().getId())
                .quantity(refundDetail.getQuantity())
                .unitPrice(refundDetail.getUnitPrice())
                .amount(refundDetail.getAmount())
                .refundId(refundDetail.getRefund().getId())
                .build();
    }
}
