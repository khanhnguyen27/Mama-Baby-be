package com.myweb.mamababy.responses.refunds;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Exchange;
import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.models.Refund;
import com.myweb.mamababy.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundResponse {

    private int id;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("exchange_id")
    private int exchangeId;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate  createDate;

    public static RefundResponse fromRefund(Refund refund){
        RefundResponse refundResponse = RefundResponse.builder()
                .id(refund.getId())
                .userId(refund.getUser().getId())
                .createDate(refund.getCreateDate())
                .build();
        if(refund.getOrder() != null){
            refundResponse.setOrderId(refund.getOrder().getId());
        }
        if(refund.getExchange() != null){
            refundResponse.setExchangeId(refund.getExchange().getId());
        }
        return refundResponse;
    }
}
