package com.myweb.mamababy.responses.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Exchange;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeResponse {

    private int id;

    @JsonProperty("order_detail_id")
    private int orderDetailId;

    private int quantity;

    @JsonProperty("image_url")
    private String imageUrl;

    private String description;

    @JsonProperty("create_date")
    private LocalDate createDate;

    @JsonProperty("user_id")
    private int userId;

    public static ExchangeResponse fromExchange(Exchange exchange){
        return ExchangeResponse.builder()
                .id(exchange.getId())
                .orderDetailId(exchange.getOrderDetail().getId())
                .quantity(exchange.getQuantity())
                .imageUrl(exchange.getImageUrl())
                .description(exchange.getDescription())
                .createDate(exchange.getCreateDate())
                .userId(exchange.getUser().getId())
                .build();
    }
}
