package com.myweb.mamababy.responses.refunds;

import com.myweb.mamababy.responses.exchange.ExchangeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class RefundListResponse {
    private List<RefundResponse> refunds;
    private int totalPages;
}
