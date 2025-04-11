package org.example.postory.domain.post.service;

import java.time.LocalDateTime;
import org.example.postory.domain.post.dto.PostRequestDto;
import org.example.postory.domain.post.dto.PostResponseDto;
import org.example.postory.domain.post.dto.PostResponseDto.NewsFeed;
import org.example.postory.domain.post.entity.Post;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface PostService {

    // 게시물 id와 사용자 id로 게시물 조회
    Post getPostById(long postId, Long userId);

    // 게시물 단건 조회
    PostResponseDto.GetPost getPost(long id, UserDetails userDetails, LocalDateTime cursorCreatedAt, Long cursorId, int size);

    // 게시물 생성
    PostResponseDto.Create createPost(PostRequestDto.Create dto, UserDetails userDetails);

    // 게시물 삭제
    void deletePost(long postId, UserDetails userDetails);

    // 뉴스피드 조회
    CursorResponseDto<NewsFeed> getNewsFeed(LocalDateTime cursorUpdatedAt, Long cursorId, int size,
        UserDetails userDetails);

    // 게시물 수정
    void updatePost(long id, PostRequestDto.Update postRequestDto, Long userId);

    // 게시물 좋아요
    void likePost(long id, UserDetails userDetails);

    /**
     * 타입에 따른 검색
     *
     * @{keyword}: 유저 검색 #{keyword}: 해시태그 검색 {keyword}: 게시물 검색
     */
    CursorResponseDto<PostResponseDto.SearchList> getSearchList(PostRequestDto.Search dto,
        LocalDateTime cursorUpdatedAt, Long cursorId);
}
