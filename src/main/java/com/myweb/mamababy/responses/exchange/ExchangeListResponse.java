package com.myweb.mamababy.responses.exchange;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ExchangeListResponse {
    private List<ExchangeResponse> exchanges;
    private int totalPages;
}
