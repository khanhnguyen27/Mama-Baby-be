package com.myweb.mamababy.services.Comment;

import com.myweb.mamababy.dtos.AgeDTO;
import com.myweb.mamababy.dtos.CommentDTO;
import com.myweb.mamababy.models.Age;
import com.myweb.mamababy.models.Comment;

import java.util.List;

public interface ICommentService {
    Comment createComment(CommentDTO commentDTO) throws Exception;
    Comment getCommentById(int Id) throws Exception;
    List<Comment> getAllComment() throws Exception;
    Comment updateComment(int Id, CommentDTO commentDTO) throws Exception;
    void deleteComment(int Id) throws Exception;
}
