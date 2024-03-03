package avia.cloud.client.service;

import avia.cloud.client.dto.CommentDTO;

import java.util.List;

public interface ICommentService {
    List<CommentDTO> fetchComments(String lan);
}
