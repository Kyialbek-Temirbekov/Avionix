package avia.cloud.client.service.impl;

import avia.cloud.client.dto.CommentDTO;
import avia.cloud.client.entity.Account;
import avia.cloud.client.entity.Comment;
import avia.cloud.client.entity.Customer;
import avia.cloud.client.entity.enums.Lan;
import avia.cloud.client.exception.NotFoundException;
import avia.cloud.client.repository.AccountRepository;
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
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CommentDTO> fetchComments(String lan) {
        return commentRepository.findAllByLanAndCheckedTrue(Lan.of(lan)).stream().map(this::convertToCommentDTO).toList();
    }

    @Override
    public List<CommentDTO> fetchCommentsByText(String text) {
        return commentRepository.findAllByText(text).stream().map(this::convertToCommentDTO).toList();
    }

    @Override
    public List<CommentDTO> findCustomerComments(String email) {
        Customer customer = accountRepository.findByEmail(email).map(Account::getCustomer)
                .orElseThrow(() -> new NotFoundException("Customer", "email", email));
        return customer.getComments().stream().map(this::convertToCommentDTO).toList();
    }

    @Override
    public void createComment(CommentDTO commentDTO, String email) {
        Comment comment = convertToComment(commentDTO);
        Customer customer = accountRepository.findByEmail(email).map(Account::getCustomer)
                .orElseThrow(() -> new NotFoundException("Customer", "email", email));
        comment.setCustomer(customer);
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(String commentId) {
        commentRepository.deleteById(commentId);
    }

    private Comment convertToComment(CommentDTO comment) {
        return modelMapper.map(comment, Comment.class);
    }

    private CommentDTO convertToCommentDTO(Comment comment) {
        Customer customer = comment.getCustomer();
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        commentDTO.setAuthor(String.join(" ", customer.getFirstName(), customer.getLastName()));
        return commentDTO;
    }
}
