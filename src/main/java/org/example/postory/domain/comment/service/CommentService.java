package org.example.postory.domain.comment.service;

import jakarta.validation.Valid;
import org.example.postory.domain.comment.dto.CommentRequestDto;
import java.time.LocalDateTime;
import org.example.postory.domain.comment.dto.CommentResponseDto;
import org.example.postory.domain.comment.dto.CommentResponseDto.CommentItem;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.springframework.security.core.userdetails.UserDetails;


public interface CommentService {

    //댓글 생성
    CommentResponseDto.CommentItem createComment(long authUserId,
        CommentRequestDto.CommentItem requestDto, Long postId);

    // 댓글 전체 조회
    CursorResponseDto<CommentItem> getComments(LocalDateTime cursorUpdatedAt, Long cursorId,
        Long postId, int size);

    // 좋아요
    void likeComment(long id, UserDetails userDetails);

    //댓글 수정
    CommentItem updateComment(Long authUserId, CommentRequestDto.CommentItem requestDto, Long commentId);

    // 댓글 삭제
    void deleteComment(Long authUserId, Long commentId);
}
