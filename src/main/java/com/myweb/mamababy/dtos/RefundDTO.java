package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefundDTO {

    @NotNull(message = "User ID is required.")
    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("exchange_id")
    private int exchangeId;

    @JsonProperty("amount")
    private float amount;

}
