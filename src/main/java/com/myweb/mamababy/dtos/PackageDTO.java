package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PackageDTO {
    @NotBlank(message = "Name package is required")
    @JsonProperty("package_name")
    private String packageName;

    @NotBlank(message = "Description is required")
    @JsonProperty("description")
    private String description;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @JsonProperty("price")
    private float price;

    @Min(value = 0, message = "Month must be greater than or equal to 0")
    @JsonProperty("month")
    private int month;
}
