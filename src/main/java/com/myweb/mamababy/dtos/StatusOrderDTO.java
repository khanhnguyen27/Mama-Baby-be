package com.myweb.mamababy.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
