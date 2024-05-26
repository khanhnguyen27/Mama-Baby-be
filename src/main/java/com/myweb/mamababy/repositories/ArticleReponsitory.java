package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Article;
import com.myweb.mamababy.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleReponsitory extends JpaRepository<Article, Integer> {
    // Method to find articles by storeId
    List<Article> findByStoreId(int storeId);

}
