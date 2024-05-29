package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeDTO {

    @NotNull(message = "Order detail ID is required.")
    @JsonProperty("order_detail_id")
    private int orderDetailId;

    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("image_url")
    private String imageUrl;

    @NotEmpty(message = "Description is required.")
    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

    @NotNull(message = "User ID is required.")
    @JsonProperty("user_id")
    private int userId;

}
