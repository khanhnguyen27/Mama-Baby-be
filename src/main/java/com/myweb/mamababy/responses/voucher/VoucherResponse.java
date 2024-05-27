package com.myweb.mamababy.responses.voucher;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherResponse {

    @JsonProperty("result")
    private int result;

    @JsonProperty("errorMessage")
    private String errorMessage;
}
