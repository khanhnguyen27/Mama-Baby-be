package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.ManyToOne;
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

    @NotEmpty(message = "Quantity is required.")
    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("image_url")
    private String imageUrl;

    @NotEmpty(message = "Description is required.")
    @JsonProperty("description")
    private String description;

    @NotNull(message = "User ID is required.")
    @JsonProperty("user_id")
    private int userId;

}
