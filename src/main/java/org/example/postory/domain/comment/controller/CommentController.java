package org.example.postory.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.comment.dto.CommentRequestDto;
import org.example.postory.domain.comment.dto.CommentResponseDto;
import org.example.postory.domain.comment.dto.CommentResponseDto.CommentItem;
import org.example.postory.domain.comment.service.CommentService;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 생성
    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDto.CommentItem> createComment(
            @RequestBody @Valid CommentRequestDto.CommentItem requestDto,
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        commentService.createComment(Long.parseLong(userDetails.getUsername()), requestDto,
                                postId));
    }

    //댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto.CommentItem> updateComment(
            @RequestBody @Valid CommentRequestDto.CommentItem requestDto,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        commentService.updateComment(Long.parseLong(userDetails.getUsername()), requestDto,
                                commentId));
    }

    // 댓글 조회
    @GetMapping
    public ResponseEntity<CursorResponseDto<CommentItem>> getComments(
            @RequestParam(required = false) LocalDateTime cursorCreatedAt,
            @RequestParam(required = false) Long cursorId,
            @RequestParam Long postId,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getComments(cursorCreatedAt, cursorId, postId, size, userDetails));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        commentService.deleteComment(Long.parseLong(userDetails.getUsername()), commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 좋아요
    @PatchMapping("/like/{id}")
    public ResponseEntity<Void> likeComment(@PathVariable("id") long id,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        commentService.likeComment(id, userDetails);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
