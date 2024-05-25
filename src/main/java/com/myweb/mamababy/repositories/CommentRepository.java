package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Comment;
import com.myweb.mamababy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    //Find by user id
    List<Comment> findByUserId(int userId);
    //Find by product id
    List<Comment> findByProductId(int productId);

    boolean existsByUserIdAndProductId(int userId, int productId);
}
