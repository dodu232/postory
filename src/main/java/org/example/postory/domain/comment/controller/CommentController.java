package org.example.postory.domain.comment.controller;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.comment.dto.CommentResponseDto.CommentItem;
import org.example.postory.domain.comment.service.CommentService;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor // 생성자 주입
public class CommentController {

    private final CommentService commentService;

    // 댓글 조회
    @GetMapping
    public ResponseEntity<CursorResponseDto<CommentItem>> getComments(
        @RequestParam(required = false)LocalDateTime cursorCreatedAt,
        @RequestParam(required = false)Long cursorId,
        @RequestParam Long postId,
        @RequestParam(defaultValue = "10")int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComments(cursorCreatedAt, cursorId, postId, size));
    }
}
