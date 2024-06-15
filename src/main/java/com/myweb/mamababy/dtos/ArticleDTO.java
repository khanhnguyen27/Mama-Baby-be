package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "content is required")
    private String content;

    @JsonProperty("link_product")
    @NotBlank(message = "link_product is required")
    private String link_product;

    @JsonProperty("link_image")
    @NotBlank(message = "image is required")
    private String link_image;

    @JsonProperty("store_id")
    @NotBlank(message = "store_id is required")
    private int store_id;

    @JsonProperty("status")
    private Boolean status;
}
