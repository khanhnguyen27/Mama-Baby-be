package com.myweb.mamababy.responses.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusOrderResponse {

    private int id;

    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("date")
    private LocalDate date;

    public static StatusOrderResponse fromStatusOrder(StatusOrder statusOrder) {
        return StatusOrderResponse.builder()
                .id(statusOrder.getId())
                .orderId(statusOrder.getOrder().getId())
                .status(statusOrder.getStatus())
                .date(statusOrder.getDate())
                .build();
    }

}