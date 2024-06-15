package com.myweb.mamababy.services.Article;

import com.myweb.mamababy.dtos.ArticleDTO;
import com.myweb.mamababy.models.Article;
import com.myweb.mamababy.responses.article.ArticleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IArticleService {
    Article createArticle(ArticleDTO articleDTO, MultipartFile file) throws Exception;

    Article getArticleById(int id) throws Exception;

    Page<ArticleResponse> getAllArticle(String keyword, int storeId, PageRequest pageRequest) throws Exception;

    Article updateArticle(int id, ArticleDTO articleDTO, String token, MultipartFile file) throws Exception;

    void deleteArticle(int id);

    String storeFile(MultipartFile file) throws IOException;

    void deleteFile(String filename) throws IOException;

    Page<ArticleResponse> getArticlesByStoreId(String keyword, int storeId, String token, PageRequest pageRequest) throws Exception;

}
