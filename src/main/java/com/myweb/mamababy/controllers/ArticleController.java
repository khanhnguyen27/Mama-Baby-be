package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.ArticleDTO;
import com.myweb.mamababy.dtos.CommentDTO;
import com.myweb.mamababy.models.Article;
import com.myweb.mamababy.models.Comment;
import com.myweb.mamababy.responses.Article.ArticleResponse;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.comment.CommentResponse;
import com.myweb.mamababy.services.Article.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    // POST http://localhost:8080/mamababy/article
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createArticle(
            @Valid @ModelAttribute ArticleDTO articleDTO,
            BindingResult result,
            @RequestParam("files") MultipartFile file
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            // Kiểm tra kích thước file và định dạng
            if (file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body(ResponseObject.builder()
                                .message("Cannot upload images >10MB !!!")
                                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                                .build());
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(ResponseObject.builder()
                                .message("The file is not suitable !!!")
                                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                .build());
            }

            // Lưu file và cập nhật thumbnail trong DTO
            String filename = articleService.storeFile(file);

            // lưu vào đối tượng product trong DB
            articleDTO.setLink_image(filename);
            Article newArticle = articleService.createArticle(articleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ResponseObject.builder()
                            .message("Create new article successfully")
                            .status(HttpStatus.CREATED)
                            .data(ArticleResponse.fromArticle(newArticle))
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message(e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }
    }

    //Ai cũng xem được
    @GetMapping
    public ResponseEntity<?> getAllArticles() throws Exception {
        List<Article> articles = articleService.getAllArticle();
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get articles successfully")
                        .data(articles.stream()
                                .map(ArticleResponse::fromArticle)
                                .collect(Collectors.toList()))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    //Store nào chỉ xem được article của shop đó
    @GetMapping("/store/{id}")
    public ResponseEntity<?> getArticlesByStore(@PathVariable("id") int id,
                                                @RequestHeader("Authorization") String token) throws Exception {
        String extractedToken = token.substring(7);
        List<Article> articles = articleService.getArticlesByStoreId(id, extractedToken);
        if (articles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseObject.builder()
                            .message("No posts found for store with id: " + id)
                            .status(HttpStatus.NOT_FOUND)
                            .build());
        } else {
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Retrieve successful posts for store with id: " + id)
                            .data(articles.stream()
                                    .map(ArticleResponse::fromArticle)
                                    .collect(Collectors.toList()))
                            .status(HttpStatus.OK)
                            .build()
            );
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateArticle(@Valid @ModelAttribute ArticleDTO articleDTO,
                                           BindingResult result,
                                           @RequestParam("files") MultipartFile file,
                                           @PathVariable("id") int id,
                                           @RequestHeader("Authorization") String token) throws Exception {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            // Kiểm tra kích thước file và định dạng
            if (file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body(ResponseObject.builder()
                                .message("Cannot upload images >10MB !!!")
                                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                                .build());
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(ResponseObject.builder()
                                .message("The file is not suitable !!!")
                                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                .build());
            }

            // Lưu file và cập nhật thumbnail trong DTO
            String filename = articleService.storeFile(file);

            String extractedToken = token.substring(7);
            // lưu vào đối tượng product trong DB
            articleDTO.setLink_image(filename);
            Article updatedArticle = articleService.updateArticle(id, articleDTO, extractedToken);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Update article successfully")
                            .data(ArticleResponse.fromArticle(updatedArticle))
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message(e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }
    }

    //Không dùng xóa cứng
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteArticle(@PathVariable("id") int id) {
//        articleService.deleteArticle(id);
//        return ResponseEntity.ok("Article deleted successfully");
//    }

}
