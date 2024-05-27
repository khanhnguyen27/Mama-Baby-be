package com.myweb.mamababy.responses.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Article;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleResponse {
    @JsonProperty("id")
    private int id;

    @JsonProperty("header")
    private String header;

    @JsonProperty("content")
    private String content;

    @JsonProperty("link_product")
    private String link_product;

    @JsonProperty("link_image")
    private String link_image;

    @JsonProperty("store_id")
    private int store_id;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    @JsonProperty("date")
    private Date date;

    @JsonProperty("status")
    private Boolean status;

    public static ArticleResponse fromArticle(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .header(article.getHeader())
                .content(article.getContent())
                .link_product(article.getLink_product())
                .link_image(article.getLink_image())
                .store_id(article.getStore().getId())
                .date(article.getDate())
                .status(article.getStatus())
                .build();
    }
}
