package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    @NotEmpty(message = "Header's name cannot be empty")
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

    @JsonProperty("status")
    private Boolean status;
}
