package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivedDTO {

    @JsonProperty("voucher_id")
    private int voucherId;

    @JsonProperty("user_id")
    private int userID;

}
