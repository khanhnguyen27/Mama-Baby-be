package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.ArticleDTO;
import com.myweb.mamababy.models.Article;
import com.myweb.mamababy.responses.Article.ArticleListResponse;
import com.myweb.mamababy.responses.Article.ArticleResponse;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.product.ProductListResponse;
import com.myweb.mamababy.responses.product.ProductResponse;
import com.myweb.mamababy.services.Article.ArticleService;
import com.myweb.mamababy.services.Article.IArticleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/article")
@RequiredArgsConstructor
public class ArticleController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
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
    public ResponseEntity<?> getAllArticles(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "store_id") int storeId,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "12", name = "limit") int limit,
            @RequestHeader("Authorization") String token
    ) throws Exception {
        int totalPages = 0;

        // Tạo Pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                //Sort.by("createdAt").descending()
                Sort.by("id").descending()
        );
        Page<ArticleResponse> articlePage = articleService.getAllArticle(keyword, storeId, pageRequest);
        // Lấy tổng số trang
        totalPages = articlePage.getTotalPages();
        List<ArticleResponse> products = articlePage.getContent();

        ArticleListResponse articleListResponse = ArticleListResponse
                .builder()
                .articles(products)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get articles successfully")
                        .data(articleListResponse)
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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                logger.info(imageName + " not found");
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpeg").toUri()));
                //return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error occurred while retrieving image: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    //Không dùng xóa cứng
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteArticle(@PathVariable("id") int id) {
//        articleService.deleteArticle(id);
//        return ResponseEntity.ok("Article deleted successfully");
//    }

}
