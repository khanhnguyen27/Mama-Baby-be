package com.myweb.mamababy.services.Comment;

import com.myweb.mamababy.components.JwtTokenUtil;
import com.myweb.mamababy.dtos.CartItemDTO;
import com.myweb.mamababy.dtos.CommentDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.exceptions.ExpiredTokenException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.CommentRepository;
import com.myweb.mamababy.repositories.ProductRepository;
import com.myweb.mamababy.repositories.UserRepository;
import com.myweb.mamababy.services.User.IUserService;
import com.myweb.mamababy.services.User.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService{
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final IUserService userService;

    @Override
    @Transactional
    public List<Comment> createComments(CommentDTO commentDTO) throws Exception {
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        List<Comment> comments = new ArrayList<>();


            int productId = commentDTO.getProductId();

//            if (commentRepository.existsByUserIdAndProductId(commentDTO.getUserId(), productId)) {
//                throw new Exception("User has already commented on product with ID: " + productId);
//            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Product not found with ID: " + productId));

            Comment newComment = Comment.builder()
                    .rating(commentDTO.getRating())
                    .comment(commentDTO.getComment())
                    .date(new Date())
                    .status(true)
                    .build();
            newComment.setProduct(product);
            newComment.setUser(user);

            comments.add(commentRepository.save(newComment));

        return comments;
    }


    @Override
    public Comment getCommentById(int Id) {
        return commentRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }
    @Override
    public List<Comment> getCommentsByUserId(int UserId, String token) throws Exception {

            User retrievedUser = userService.getUserDetailsFromToken(token);
            if (retrievedUser.getId() != UserId) {
                throw new Exception("Store does not match");
            } else {
                List<Comment> comments = commentRepository.findByUserId(retrievedUser.getId());
                return handleEmptyList(comments);
            }

    }

    @Override
    public List<Comment> getCommentsByProductId(int productId) {
        List<Comment> comments = commentRepository.findByProductId(productId);
        return handleEmptyList(comments);
    }

    @Override
    public List<Comment> getCommentsByProductIdWithStore(int productId) {
        List<Comment> comments = commentRepository.findByProductIdWithStore(productId);
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
        List<Comment> comments = commentRepository.findAllByStatusTrue();
        return handleEmptyList(comments);
    }

    @Override
    public Comment updateComment(int Id, CommentDTO commentDTO, String token) throws Exception {

        User user = userService.getUserDetailsFromToken(token);
        List<Comment> existingCom1 = commentRepository.findByUserId(commentDTO.getUserId());
        Comment existingCom;

        // Kiểm tra xem danh sách existingCom1 có chứa Id hay không
        boolean isIdExist = existingCom1.stream()
                .anyMatch(comment -> comment.getId() == Id);

        if (!isIdExist) {
            throw new Exception("Comment with id " + Id + " not found for user " + commentDTO.getUserId());
        }
            if (user.getId() != commentDTO.getUserId()) {
                throw new Exception("User does not match");
            } else {

                existingCom = getCommentById(Id);
                existingCom.setComment(commentDTO.getComment());
                existingCom.setRating(commentDTO.getRating());
                commentRepository.save(existingCom);
                return existingCom;
            }
    }

    @Override
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
