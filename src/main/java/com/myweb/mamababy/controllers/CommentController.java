package com.myweb.mamababy.controllers;


import com.myweb.mamababy.dtos.CommentDTO;
import com.myweb.mamababy.models.Comment;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.comment.CommentResponse;
import com.myweb.mamababy.responses.store.StoreResponse;
import com.myweb.mamababy.services.Comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<?> getAllComments() {
        List<Comment> comments = commentService.getAllComment();
        if (comments == null || comments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseObject.builder()
                            .message("No comments found")
                            .status(HttpStatus.NOT_FOUND)
                            .build());
        }

        return ResponseEntity.ok(comments);
    }


    @GetMapping("/product/{id}") // Lấy tất cả bình luận của sản phẩm
    public ResponseEntity<?> getCommentByProduct(@PathVariable("id") int id) {
        List<Comment> comments = commentService.getCommentsByProductId(id);
        if (comments == null || comments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseObject.builder()
                            .message("No comments found for product with ID: " + id)
                            .status(HttpStatus.NOT_FOUND)
                            .build());
        }

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get comments of product successfully")
                        .data(comments.stream()
                                .map(CommentResponse::fromComment)
                                .collect(Collectors.toList()))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/user/{id}") // Lấy tất cả bình luận của người dùng
    public ResponseEntity<?> getCommentByUser(@PathVariable("id") int id) {
        List<Comment> comments = commentService.getCommentsByUserId(id);
        if (comments == null || comments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseObject.builder()
                            .message("No comments found for user with ID: " + id)
                            .status(HttpStatus.NOT_FOUND)
                            .build());
        }

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get comments of user successfully")
                        .data(comments.stream()
                                .map(CommentResponse::fromComment)
                                .collect(Collectors.toList()))
                        .status(HttpStatus.OK)
                        .build()
        );
    }


    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO) throws Exception {
        try {
            Comment createdComment = commentService.createComment(commentDTO);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Create comments successfully")
                            .data(CommentResponse.fromComment(createdComment))
                            .status(HttpStatus.OK)
                            .build()
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ResponseObject.builder()
                            .message("User has already commented on this product")
                            .status(HttpStatus.CONFLICT)
                            .build()
            );
        }
    }

    //update comment for user
    //Users can only update comments
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") int id, @RequestBody CommentDTO commentDTO) {
        Comment updatedComment = commentService.updateComment(id, commentDTO);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Update comments successfully")
                        .data(CommentResponse.fromComment(updatedComment))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    //update status comment for staff, admin, block comment
    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateCommentStatus(@PathVariable("id") int id, @RequestBody String status) {
        boolean parsedStatus = Boolean.parseBoolean(status);
        Comment updatedComment = commentService.updateCommentStatus(id, parsedStatus);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Update comments successfully")
                        .data(CommentResponse.fromComment(updatedComment))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") int id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
