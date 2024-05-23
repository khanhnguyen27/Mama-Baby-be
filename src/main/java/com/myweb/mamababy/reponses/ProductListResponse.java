package com.myweb.mamababy.reponses;

import com.myweb.mamababy.models.Product;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPages;
}