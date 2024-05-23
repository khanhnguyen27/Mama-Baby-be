package com.myweb.mamababy.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandDTO {
    @NotEmpty(message = "Brand's name cannot be empty")
    private String name;
}
