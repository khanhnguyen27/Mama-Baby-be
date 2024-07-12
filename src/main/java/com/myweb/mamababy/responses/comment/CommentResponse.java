package com.myweb.mamababy.responses.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Comment;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @JsonProperty("date")
    private Date date;

    @JsonProperty("status")
    private Boolean status;

    public static List<CommentResponse> fromComment(List<Comment> comments) {
        return comments.stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .product_id(comment.getProduct().getId())
                        .rating(comment.getRating())
                        .comment(comment.getComment())
                        .user_id(comment.getUser().getId())
                        .date(comment.getDate())
                        .status(comment.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

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
