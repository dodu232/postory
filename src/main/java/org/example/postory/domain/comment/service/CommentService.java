package org.example.postory.domain.comment.service;

import org.example.postory.domain.comment.dto.CommentRequestDto.Create;
import org.example.postory.domain.comment.dto.CommentResponseDto;

public interface CommentService {

    CommentResponseDto.Create createComment(long authUserId, Create requestDto, Long postId);
}
