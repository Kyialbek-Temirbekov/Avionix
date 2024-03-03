package avia.cloud.client.controller;

import avia.cloud.client.dto.CommentDTO;
import avia.cloud.client.service.ICommentService;
import avia.cloud.client.validation.constraint.SupportedLanguage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
