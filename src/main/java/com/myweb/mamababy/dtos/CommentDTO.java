package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    @NotBlank(message = "product_id is required")
    @JsonProperty("product_id")
    private int productId;

    @NotBlank(message = "rating is required")
    @JsonProperty("rating")
    private int rating;

    @NotBlank(message = "comment is required")
    @JsonProperty("comment")
    private String comment;

    @NotBlank(message = "user_id is required")
    @JsonProperty("user_id")
    private int userId;
}
