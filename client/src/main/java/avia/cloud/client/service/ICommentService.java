package avia.cloud.client.service;

import avia.cloud.client.dto.CommentDTO;

import java.util.List;

public interface ICommentService {
    List<CommentDTO> fetchComments(String lan);
    List<CommentDTO> fetchCommentsByText(String text);
    List<CommentDTO> findCustomerComments(String email);
    void createComment(CommentDTO comment, String email);
    void deleteComment(String commentId);
    List<CommentDTO> fetchAllComments();
    void checkComment(String commentId);
}
