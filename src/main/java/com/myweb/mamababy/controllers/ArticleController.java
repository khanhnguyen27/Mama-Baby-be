package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.ArticleDTO;
import com.myweb.mamababy.models.Article;
import com.myweb.mamababy.responses.Article.ArticleResponse;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.services.Article.ArticleService;
import com.myweb.mamababy.services.Article.IArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final IArticleService articleService;

    // POST http://localhost:8080/mamababy/article
    @CrossOrigin(origins = "http://localhost:3000")
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
            Article newArticle = articleService.createArticle(articleDTO, file);
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
    @CrossOrigin(origins = "http://localhost:3000")
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
    @CrossOrigin(origins = "http://localhost:3000")
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

    @CrossOrigin(origins = "http://localhost:3000")
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
            String extractedToken = token.substring(7);
            Article updatedArticle = articleService.updateArticle(id, articleDTO, extractedToken, file);
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
