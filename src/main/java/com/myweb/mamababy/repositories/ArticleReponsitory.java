package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Article;
import com.myweb.mamababy.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleReponsitory extends JpaRepository<Article, Integer> {
    // Method to find articles by storeId
    List<Article> findByStoreId(int storeId);

    @Query("SELECT p FROM Article p WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR p.header LIKE %:keyword%) " +
            "AND (:storeId IS NULL OR :storeId = 0 OR p.store.id = :storeId)" +
            "AND p.status = true")
    Page<Article> searchArticles(String keyword, int storeId, PageRequest pageRequest);

    @Query("SELECT p FROM Article p WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR p.header LIKE %:keyword%) " +
            "AND (:storeId IS NULL OR :storeId = 0 OR p.store.id = :storeId)")
    Page<Article> searchArticlesByStore(String keyword, int storeId, PageRequest pageRequest);

    @Query("SELECT p FROM Article p WHERE " +
            "p.status = true")
    List<Article> searchArticlesNoPage();
}
