package com.myweb.mamababy.responses.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.StatusOrder;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @JsonProperty("date")
    private LocalDateTime date;

    public static StatusOrderResponse fromStatusOrder(StatusOrder statusOrder) {
        return StatusOrderResponse.builder()
                .id(statusOrder.getId())
                .orderId(statusOrder.getOrder().getId())
                .status(statusOrder.getStatus())
                .date(statusOrder.getDate())
                .build();
    }

}
