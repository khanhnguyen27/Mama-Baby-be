package com.myweb.mamababy.responses.refunds;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Refund;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundResponse {

    private int id;

    private String description;

    private String status;

    private float amount;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate  createDate;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("store_id")
    private int storeId;

    @JsonProperty("refund_detail_list")
    private List<RefundDetailResponse> refundDetails;

    public static RefundResponse fromRefund(Refund refund){
        return RefundResponse.builder()
                .id(refund.getId())
                .description(refund.getDescription())
                .status(refund.getStatus())
                .amount(refund.getAmount())
                .createDate(refund.getCreateDate())
                .userId(refund.getUser().getId())
                .storeId(refund.getStore().getId())
                .orderId(refund.getOrder().getId())
                .refundDetails(refund.getRefundDetails().stream().map(RefundDetailResponse::fromRefundDetail).toList())
                .build();
    }
}
