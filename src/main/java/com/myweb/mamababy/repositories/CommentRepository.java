package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    //Find by user id
    @Query("SELECT c FROM Comment c WHERE c.user.id = :user_id AND c.status = true")
    List<Comment> findByUserId(int user_id);
    //Find by product id
    @Query("SELECT c FROM Comment c WHERE c.product.id = :product_id AND c.status = true")
    List<Comment> findByProductId(int product_id);

    @Query("SELECT c FROM Comment c WHERE c.product.id = :product_id")
    List<Comment> findByProductIdWithStore(int product_id);

    @Query("SELECT c FROM Comment c WHERE c.status = true")
    List<Comment> findAllByStatusTrue();

    boolean existsByUserIdAndProductId(int userId, int productId);
}
