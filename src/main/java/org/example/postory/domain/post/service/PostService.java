package org.example.postory.domain.post.service;

import java.time.LocalDateTime;
import java.util.List;
import org.example.postory.domain.post.dto.PostResponseDto;
import org.example.postory.domain.post.dto.PostResponseDto.ProfileInquiry;
import org.example.postory.domain.post.entity.Post;
import org.example.postory.global.common.pagination.CursorResponseDto;

public interface PostService {

    // 게시물 id와 사용자 id로 게시물 조회
    Post getPostById(long postId, Long userId);

    CursorResponseDto<ProfileInquiry> getNewsFeed(LocalDateTime cursorUpdatedAt, Long cursorId);

}
