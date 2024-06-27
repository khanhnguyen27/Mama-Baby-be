package com.myweb.mamababy.responses.exchange;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Exchange;
import com.myweb.mamababy.models.ExchangeDetail;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeResponse {

    private int id;

    private String description;

    private String status;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @JsonProperty("create_date")
    private LocalDateTime createDate;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("store_id")
    private int storeId;

    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("exchange_detail_list")
    private List<ExchangeDetailResponse> exchangeDetails;

    public static ExchangeResponse fromExchange(Exchange exchange){
        return ExchangeResponse.builder()
                .id(exchange.getId())
                .description(exchange.getDescription())
                .status(exchange.getStatus())
                .createDate(exchange.getCreateDate())
                .userId(exchange.getUser().getId())
                .storeId(exchange.getStore().getId())
                .orderId(exchange.getOrder().getId())
                .exchangeDetails(exchange.getExchangeDetails().stream().map(ExchangeDetailResponse::fromExchangeDetail).toList())
                .build();
    }
}
