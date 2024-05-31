package com.myweb.mamababy.services.Comment;

import com.myweb.mamababy.components.JwtTokenUtil;
import com.myweb.mamababy.dtos.CommentDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.exceptions.ExpiredTokenException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.CommentRepository;
import com.myweb.mamababy.repositories.ProductRepository;
import com.myweb.mamababy.repositories.UserRepository;
import com.myweb.mamababy.services.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService{
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

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

    public List<Comment> getCommentsByUserId(int UserId, String token) throws Exception {

            User retrievedUser = userService.getUserDetailsFromToken(token);
            if (retrievedUser.getId() != UserId) {
                throw new Exception("Store does not match");
            } else {
                List<Comment> comments = commentRepository.findByUserId(retrievedUser.getId());
                return handleEmptyList(comments);
            }

    }


    public List<Comment> getCommentsByProductId(int productId) {
        List<Comment> comments = commentRepository.findByProductId(productId);
        return handleEmptyList(comments);
    }

    private List<Comment> handleEmptyList(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return Collections.emptyList(); // hoặc trả về null
        }
        return comments;
    }

    @Override
    public List<Comment> getAllComment() {
        List<Comment> comments = commentRepository.findAll();
        return handleEmptyList(comments);
    }

    @Override
    public Comment updateComment(int Id, CommentDTO commentDTO, String token) throws Exception {

        User user = userService.getUserDetailsFromToken(token);
        Comment existingCom;
            if (user.getId() != commentDTO.getUserId()) {
                throw new Exception("User does not match");
            } else {

                existingCom = getCommentById(Id);
                existingCom.setComment(commentDTO.getComment());
                commentRepository.save(existingCom);
                return existingCom;
            }
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
