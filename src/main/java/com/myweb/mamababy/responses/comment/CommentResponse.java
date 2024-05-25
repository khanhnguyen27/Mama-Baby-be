package com.myweb.mamababy.responses.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Comment;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.responses.user.UserResponse;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    @JsonProperty("id")
    private int id;

    @JsonProperty("product_id")
    private int product_id;

    @JsonProperty("rating")
    private int rating;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("user_id")
    private int user_id;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    @JsonProperty("date")
    private Date date;

    @JsonProperty("status")
    private Boolean status;

    public static CommentResponse fromComment(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .product_id(comment.getProduct().getId())
                .rating(comment.getRating())
                .comment(comment.getComment())
                .user_id(comment.getUser().getId())
                .date(comment.getDate())
                .status(comment.getStatus())
                .build();
    }
}
