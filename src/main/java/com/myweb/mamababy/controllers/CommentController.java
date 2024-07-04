package com.myweb.mamababy.controllers;


import com.myweb.mamababy.dtos.CommentDTO;
import com.myweb.mamababy.exceptions.ExpiredTokenException;
import com.myweb.mamababy.models.Comment;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.comment.CommentResponse;
import com.myweb.mamababy.responses.store.StoreResponse;
import com.myweb.mamababy.services.Comment.CommentService;
import com.myweb.mamababy.services.Comment.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final ICommentService commentService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<?> getAllComments() throws Exception {
        List<Comment> comments = commentService.getAllComment();
        if (comments == null || comments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseObject.builder()
                            .message("No comments found")
                            .status(HttpStatus.OK)
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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getCommentByProduct(@PathVariable("id") int id) throws Exception {
        List<Comment> comments = commentService.getCommentsByProductId(id);
        if (comments == null || comments.isEmpty()) {
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .message("No comments found for product with ID: " + id)
                            .status(HttpStatus.OK)
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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/store/product/{id}")
    public ResponseEntity<?> getCommentByProductWithStore(@PathVariable("id") int id) throws Exception {
        List<Comment> comments = commentService.getCommentsByProductIdWithStore(id);
        if (comments == null || comments.isEmpty()) {
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .message("No comments found for product with ID: " + id)
                            .status(HttpStatus.OK)
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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getCommentByUser(@PathVariable("id") int UserId,
                                              @RequestHeader("Authorization") String token) throws Exception {
        String extractedToken = token.substring(7);
        List<Comment> comments = commentService.getCommentsByUserId(UserId, extractedToken);
        if (comments == null || comments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseObject.builder()
                            .message("No comments found for user with ID: " + UserId)
                            .status(HttpStatus.OK)
                            .build());
        }

        comments.sort(Comparator.comparingInt(Comment::getId).reversed());

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


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO) throws Exception {
        try {
            List<Comment> createdComment = commentService.createComments(commentDTO);
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
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") int id,
                                           @RequestBody CommentDTO commentDTO,
                                           @RequestHeader("Authorization") String token) throws Exception {
        String extractedToken = token.substring(7);
        Comment updatedComment = commentService.updateComment(id, commentDTO, extractedToken);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Update comments successfully")
                        .data(CommentResponse.fromComment(updatedComment))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateCommentStatus(@PathVariable("id") int id, @RequestBody Map<String, Boolean> requestBody) throws Exception {
        Boolean parsedStatus = requestBody.get("status");
        Comment updatedComment = commentService.updateCommentStatus(id, parsedStatus);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Update comments successfully")
                        .data(CommentResponse.fromComment(updatedComment))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteComment(@PathVariable("id") int id) {
//        commentService.deleteComment(id);
//        return ResponseEntity.ok("Comment deleted successfully");
//    }
}
