package com.myweb.mamababy.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusOrderDTO {

    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("date")
    private LocalDate date;


}
