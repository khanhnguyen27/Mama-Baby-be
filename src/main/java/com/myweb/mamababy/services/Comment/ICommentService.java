package com.myweb.mamababy.services.Comment;

import com.myweb.mamababy.dtos.AgeDTO;
import com.myweb.mamababy.dtos.CommentDTO;
import com.myweb.mamababy.models.Age;
import com.myweb.mamababy.models.Comment;

import java.util.List;

public interface ICommentService {
    List<Comment> createComments(CommentDTO commentDTO) throws Exception;
    Comment getCommentById(int Id) throws Exception;
    List<Comment> getAllComment() throws Exception;
    Comment updateComment(int Id, CommentDTO commentDTO, String extractedToken) throws Exception;
    void deleteComment(int Id) throws Exception;
    List<Comment> getCommentsByProductId(int productId) throws Exception;
    List<Comment> getCommentsByUserId(int UserId, String token) throws Exception;
    Comment updateCommentStatus(int Id, Boolean status) throws Exception;
}
