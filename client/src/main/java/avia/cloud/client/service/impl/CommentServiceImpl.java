package avia.cloud.client.service.impl;

import avia.cloud.client.dto.CommentDTO;
import avia.cloud.client.entity.Comment;
import avia.cloud.client.entity.Customer;
import avia.cloud.client.entity.enums.Lan;
import avia.cloud.client.repository.CommentRepository;
import avia.cloud.client.service.ICommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements ICommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CommentDTO> fetchComments(String lan) {
        return commentRepository.findAllByLan(Lan.of(lan)).stream().map(this::convertToCommentDTO).toList();
    }

    private CommentDTO convertToCommentDTO(Comment comment) {
        Customer customer = comment.getCustomer();
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        commentDTO.setAuthor(String.join(" ", customer.getFirstName(), customer.getLastName()));
        return commentDTO;
    }
}
