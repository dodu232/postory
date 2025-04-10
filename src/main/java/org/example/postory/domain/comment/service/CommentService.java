package org.example.postory.domain.comment.service;

import org.example.postory.domain.comment.dto.CommentRequestDto;
import org.example.postory.domain.comment.dto.CommentResponseDto;

public interface CommentService {

    CommentResponseDto.Create createComment(long authUserId, CommentRequestDto.Create requestDto);
}
