package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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

    @NotEmpty(message = "description is required")
    @JsonProperty("description")
    private String description;

    @NotEmpty(message = "Number phone is required")
    @JsonProperty("phone")
    private String phone;

    @JsonProperty("status")
    private String status;

    @JsonProperty("is_active")
    private boolean isActive;

    @NotNull(message = "User ID is required")
    @JsonProperty("user_id")
    private int userId;
}
