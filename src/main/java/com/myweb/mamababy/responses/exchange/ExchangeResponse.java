package com.myweb.mamababy.responses.exchange;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Exchange;
import com.myweb.mamababy.models.ExchangeDetail;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeResponse {

    private int id;

    private String description;

    private float amount;

    private String status;

    @JsonProperty("create_date")
    private LocalDate createDate;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("store_id")
    private int storeId;

    @JsonProperty("exchange_detail_list")
    private List<ExchangeDetailResponse> exchangeDetails;

    public static ExchangeResponse fromExchange(Exchange exchange){
        return ExchangeResponse.builder()
                .id(exchange.getId())
                .description(exchange.getDescription())
                .amount(exchange.getAmount())
                .status(exchange.getStatus())
                .createDate(exchange.getCreateDate())
                .userId(exchange.getUser().getId())
                .storeId(exchange.getStore().getId())
                .exchangeDetails(exchange.getExchangeDetails().stream().map(ExchangeDetailResponse::fromExchangeDetail).toList())
                .build();
    }
}
