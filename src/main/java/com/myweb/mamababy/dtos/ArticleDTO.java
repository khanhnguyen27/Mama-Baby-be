package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {

    @JsonProperty("header")
    private String header;

    @JsonProperty("content")
    private String content;

    @JsonProperty("product_recom")
    private int product_recom;

    @JsonProperty("link_image")
    private String link_image;

    @JsonProperty("store_id")
    private int store_id;

    @JsonProperty("status")
    private Boolean status;
}
