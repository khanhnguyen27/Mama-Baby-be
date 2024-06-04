package com.myweb.mamababy.responses.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.ExchangeDetail;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeDetailResponse {

    private int id;

    @JsonProperty("order_detail_id")
    private int orderDetailId;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("exchange_id")
    private int exchangeId;

    public static ExchangeDetailResponse fromExchangeDetail(ExchangeDetail exchangeDetail){
        return ExchangeDetailResponse.builder()
                .id(exchangeDetail.getId())
                .orderDetailId(exchangeDetail.getOrderDetail().getId())
                .quantity(exchangeDetail.getQuantity())
                .exchangeId(exchangeDetail.getExchange().getId())
                .build();
    }
}
