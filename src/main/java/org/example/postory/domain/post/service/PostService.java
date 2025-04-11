package org.example.postory.domain.post.service;

import java.time.LocalDateTime;

import java.util.List;
import org.example.postory.domain.post.dto.PostRequestDto;
import org.example.postory.domain.post.dto.PostResponseDto;
import org.example.postory.domain.post.dto.PostResponseDto.NewsFeed;
import org.example.postory.domain.post.entity.Post;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface PostService {
    // 게시물 id와 사용자 id로 게시물 조회
    Post getPostById(long postId, Long userId);

    // 게시물 생성
    PostResponseDto.Get createPost(PostRequestDto dto, UserDetails userDetails);

    // 게시물 삭제
    void deletePost(long postId, UserDetails userDetails);

    // 뉴스피드 조회
    CursorResponseDto<NewsFeed> getNewsFeed(LocalDateTime cursorUpdatedAt, Long cursorId, int size);

    // 게시물 좋아요
    void likePost(long id, UserDetails userDetails);

    // 삭제되지 않은 게시글 + 수정일 기준 최신 정렬
    List<NewsFeed> getAllMyPosts(Long userId);

    //공개 게시글 + 삭제되지 않은 게시글 + 수정일 기준 최신순 정렬
    List<NewsFeed> getVisiblePostsByUser(Long userId);
}
