package com.myweb.mamababy.responses.Article;

import com.myweb.mamababy.responses.product.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ArticleListResponse {
    private List<ArticleResponse> articles;
    private int totalPages;
}
