package com.myweb.mamababy.services.Article;

import com.myweb.mamababy.dtos.ArticleDTO;
import com.myweb.mamababy.models.Article;
import com.myweb.mamababy.models.Comment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IArticleService {
    Article createArticle(ArticleDTO articleDTO) throws Exception;

    Article getArticleById(int id) throws Exception;

    List<Article> getAllArticle() throws Exception;

    Article updateArticle(int id, ArticleDTO articleDTO, String token) throws Exception;

    void deleteArticle(int id);

    String storeFile(MultipartFile file) throws IOException;

    void deleteFile(String filename) throws IOException;

    List<Article> getArticlesByStoreId(int storeId, String token) throws Exception;

}
