package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreDTO {
    @NotBlank(message = "Name store is required")
    @JsonProperty("name_store")
    private String nameStore;

    @NotBlank(message = "Address is required")
    @JsonProperty("address")
    private String address;

    @JsonProperty("description")
    private String description;

    @JsonProperty("phone")
    private String phone;

    @NotNull(message = "User ID is required")
    @JsonProperty("user_id")
    private Long userId;
}
