package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VoucherDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("store_id")
    private int store_id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("discount_value")
    private int discount_value;

    @JsonProperty("description")
    private String description;

    @JsonProperty("end_at")
    private Date end_at;
}
