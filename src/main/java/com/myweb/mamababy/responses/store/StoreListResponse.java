package com.myweb.mamababy.responses.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class StoreListResponse {
    private List<StoreResponse> stores;
    private int totalPages;
}
