package com.myweb.mamababy.services.Article;

import com.myweb.mamababy.components.JwtTokenUtil;
import com.myweb.mamababy.dtos.ArticleDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.*;
import com.myweb.mamababy.responses.article.ArticleResponse;
import com.myweb.mamababy.responses.product.ProductResponse;
import com.myweb.mamababy.services.User.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ArticleService implements IArticleService{

    private final ArticleReponsitory articleRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final IUserService userService;


    private static final String UPLOADS_FOLDER = "uploads";
    @Override
    public Article createArticle(ArticleDTO articleDTO, MultipartFile file) throws Exception {
        Store existingStore = storeRepository
                .findById(articleDTO.getStore_id())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find store with id: "+ articleDTO.getStore_id()));

        String fileName = file.getName();
        if (!fileName.startsWith("Article")) {
            fileName = storeFile(file);
        }
        Article newArticle = Article.builder()
                .header(articleDTO.getHeader())
                .content(articleDTO.getContent())
                .product_recom(articleDTO.getProduct_recom())
                .link_image(fileName)
                .store(existingStore)
                .created_at(new Date())
                .updated_at(new Date())
                .status(articleDTO.getStatus())
                .build();
        return articleRepository.save(newArticle);
    }

    @Override
    public Article getArticleById(int id) throws Exception {
        return articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
    }

    @Override
    public Page<ArticleResponse> getArticlesByStoreId(String keyword, int storeId, String token, Date minDate, Date maxDate, PageRequest pageRequest) throws Exception {


        User retrievedUser = userService.getUserDetailsFromToken(token);
        Store existingStore = storeRepository
                .findByUserId(retrievedUser.getId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find store with id: "+ retrievedUser.getId()));
        if (existingStore.getId() != storeId) {
            throw new Exception("Store does not match");
        } else {
            Page<Article> articlesPage;
            if (minDate != null && maxDate != null) {
                articlesPage = articleRepository.searchArticlesByStoreAndDateRange(keyword, storeId, minDate, maxDate, pageRequest);
            } else {
                articlesPage = articleRepository.searchArticlesByStore(keyword, storeId, pageRequest);
            }
            return articlesPage.map(ArticleResponse::fromArticle);
        }

    }

    @Override
    public Page<ArticleResponse> getAllArticle(String keyword, int storeId, PageRequest pageRequest) throws Exception {
        Page<Article> articlesPage= articleRepository.searchArticles(keyword, storeId, pageRequest);
        return articlesPage.map(ArticleResponse::fromArticle);
    }

    @Override
    public List<ArticleResponse> getAllArticleNoPage() throws Exception {
        List<Article> articles= articleRepository.searchArticlesNoPage();
        List<ArticleResponse> articleResponses = articles.stream()
                .map(ArticleResponse::fromArticle)
                .toList();
        return articleResponses;
    }

    @Override
    public Article updateArticle(int id, ArticleDTO articleDTO, String token, MultipartFile file) throws Exception {

        User retrievedUser = userService.getUserDetailsFromToken(token);
        Store existingStore = storeRepository
                .findByUserId(retrievedUser.getId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find store with id: "+ retrievedUser.getId()));
        if (existingStore.getId() != (articleDTO.getStore_id())) {
            throw new Exception("Store does not match");
        } else {
            Article existingArticle = getArticleById(id);
            existingArticle.setHeader(articleDTO.getHeader());
            existingArticle.setContent(articleDTO.getContent());
            existingArticle.setProduct_recom(articleDTO.getProduct_recom());
            existingArticle.setUpdated_at(new Date());
            existingArticle.setStatus(articleDTO.getStatus());

            if(file != null && !file.isEmpty()) {
                deleteFile(existingArticle.getLink_image());
                String fileName = storeFile(file);
                existingArticle.setLink_image(fileName);
            }

            articleRepository.save(existingArticle);
            return existingArticle;
        }

    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        if (!checkFileImage(file)) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = "Article_" + UUID.randomUUID().toString() + "_" + filename;

        java.nio.file.Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    public Boolean checkFileImage(MultipartFile file) {
        Boolean result = false;
        if(file.getSize() > 10 * 1024 * 1024 || file.getOriginalFilename() == null) {
            return result;
        }
        String contentType = file.getContentType();
        if(contentType == null || !contentType.startsWith("image/")) {
            return result;
        }

        result =true;
        return result;
    }

    @Override
    public void deleteFile(String filename) throws IOException {

        Path uploadDir = Paths.get(UPLOADS_FOLDER);
        Path filePath = uploadDir.resolve(filename);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    @Override
    public void deleteArticle(int id) {
        articleRepository.deleteById(id);
    }
}