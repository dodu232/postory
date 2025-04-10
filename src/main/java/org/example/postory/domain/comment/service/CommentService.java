package org.example.postory.domain.comment.service;

import org.example.postory.domain.comment.dto.CommentRequestDto.Create;
import org.example.postory.domain.comment.dto.CommentResponseDto;
import java.time.LocalDateTime;
import org.example.postory.domain.comment.dto.CommentResponseDto.CommentItem;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.springframework.security.core.userdetails.UserDetails;


public interface CommentService {

    //댓글 생성
    CommentResponseDto.Create createComment(long authUserId, Create requestDto, Long postId);

    // 댓글 전체 조회
    CursorResponseDto<CommentItem> getComments(LocalDateTime cursorUpdatedAt, Long cursorId, Long postId, int size);

    // 좋아요
    void likeComment(long id, UserDetails userDetails);
}
