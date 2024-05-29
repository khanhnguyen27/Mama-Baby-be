package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgeDTO {
    @NotEmpty(message = "Age's name cannot be empty")
    @JsonProperty("range_age")
    private String rangeAge;

    @JsonProperty("is_active")
    private boolean isActive;
}
