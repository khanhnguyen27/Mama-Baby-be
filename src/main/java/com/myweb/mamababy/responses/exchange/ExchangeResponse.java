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

    @JsonProperty("image_url")
    private String imageUrl;

    private String description;

    private float amount;

    private String status;

    @JsonProperty("create_date")
    private LocalDate createDate;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("store_id")
    private int storeId;

    public static ExchangeResponse fromExchange(Exchange exchange){
        return ExchangeResponse.builder()
                .id(exchange.getId())
                .imageUrl(exchange.getImageUrl())
                .description(exchange.getDescription())
                .amount(exchange.getAmount())
                .status(exchange.getStatus())
                .createDate(exchange.getCreateDate())
                .userId(exchange.getUser().getId())
                .storeId(exchange.getStore().getId())
                .build();
    }
}
