package avia.cloud.client.controller;

import avia.cloud.client.dto.CommentDTO;
import avia.cloud.client.service.ICommentService;
import avia.cloud.client.validation.constraint.SupportedLanguage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Validated
public class CommentController {
    private final ICommentService iCommentService;
    @GetMapping()
    public ResponseEntity<List<CommentDTO>> fetchComments(@RequestParam @SupportedLanguage String lan) {
        return ResponseEntity.status(HttpStatus.OK).body(iCommentService.fetchComments(lan));
    }
    @GetMapping("/global")
    public ResponseEntity<List<CommentDTO>> fetchCommentsByText(@RequestParam String text) {
        return ResponseEntity.status(HttpStatus.OK).body(iCommentService.fetchCommentsByText(text));
    }
    @GetMapping("/user")
    public ResponseEntity<List<CommentDTO>> fetchUserComments(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(iCommentService.findCustomerComments(authentication.getName()));
    }
    @PostMapping()
    public ResponseEntity<Void> createComment(@Valid @RequestBody CommentDTO commentDTO, Authentication authentication) {
        iCommentService.createComment(commentDTO,authentication.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
    @DeleteMapping()
    public ResponseEntity<Void> deleteComment(@RequestParam String commentId) {
        iCommentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
