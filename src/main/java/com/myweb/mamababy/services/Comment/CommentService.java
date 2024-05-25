package com.myweb.mamababy.services.Comment;

import com.myweb.mamababy.dtos.AgeDTO;
import com.myweb.mamababy.dtos.CommentDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.AgeRepository;
import com.myweb.mamababy.repositories.CommentRepository;
import com.myweb.mamababy.repositories.ProductRepository;
import com.myweb.mamababy.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService{
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public Comment createComment(CommentDTO commentDTO) throws Exception{
        if(commentRepository.existsByUserIdAndProductId(commentDTO.getUserId(), commentDTO.getProductId()
        )){
            throw new Exception("User has already commented on this product");
        }
        Product product = productRepository.findById(commentDTO.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("Product not found"));
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Comment newCom = Comment
                .builder()
                .rating(commentDTO.getRating())
                .comment(commentDTO.getComment())
                .date(new Date())
                .status(true)
                .build();
        newCom.setProduct(product);
        newCom.setUser(user);
        return commentRepository.save(newCom);
    }

    @Override
    public Comment getCommentById(int Id) {
        return commentRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public List<Comment> getCommentsByUserId(int userId) {
        return commentRepository.findByUserId(userId);
    }

    public List<Comment> getCommentsByProductId(int productId) {
        return commentRepository.findByProductId(productId);
    }

    @Override
    public List<Comment> getAllComment() {
        return commentRepository.findAll();
    }

    @Override
    public Comment updateComment(int Id, CommentDTO commentDTO) {
        Comment existingCom = getCommentById(Id);
        existingCom.setComment(commentDTO.getComment());
        commentRepository.save(existingCom);
        return existingCom;
    }

    public Comment updateCommentStatus(int Id, Boolean status) {
        Comment existingCom = getCommentById(Id);
        existingCom.setStatus(status);
        commentRepository.save(existingCom);
        return existingCom;
    }

    @Override
    public void deleteComment(int Id) {
        commentRepository.deleteById(Id);
    }
}