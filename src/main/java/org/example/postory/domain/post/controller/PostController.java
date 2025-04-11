package org.example.postory.domain.post.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.post.dto.PostRequestDto;
import org.example.postory.domain.post.dto.PostResponseDto;
import org.example.postory.domain.post.dto.PostResponseDto.SearchList;
import org.example.postory.domain.post.dto.PostResponseDto.GetPost;
import org.example.postory.domain.post.dto.PostResponseDto.NewsFeed;
import org.example.postory.domain.post.service.PostService;
import org.example.postory.domain.user.entity.User;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.error.response.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor  // 생성자 주입
public class PostController {

    private final PostService postService;

    // 게시물 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<GetPost> getPostById(
        @PathVariable("id") long id,
        @RequestParam(required = false) LocalDateTime cursorCreatedAt,
        @RequestParam(required = false) Long cursorId,
        @RequestParam(defaultValue = "10") int size,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.getPost(id, userDetails, cursorCreatedAt, cursorId, size));
    }

    // 뉴스피드 조회
    @GetMapping
    public ResponseEntity<CursorResponseDto<NewsFeed>> getNewsFeed(
        @RequestParam(required = false) LocalDateTime cursorUpdatedAt,
        @RequestParam(required = false) Long cursorId,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(postService.getNewsFeed(cursorUpdatedAt, cursorId, size));
    }

    // 게시물 생성
    @PostMapping
    public ResponseEntity<PostResponseDto.Create> createPost(
        @Valid @RequestBody PostRequestDto.Create request,
        @AuthenticationPrincipal UserDetails userDetails) {
        PostResponseDto.Create response = postService.createPost(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 게시물 수정
    @PatchMapping("/{id}")
    public ResponseEntity<String> updatePost(
        @PathVariable("id") long id,
        @RequestBody @Valid PostRequestDto.Update request,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        postService.updatePost(id, request, Long.valueOf(userDetails.getUsername()));
        return ResponseEntity.status(HttpStatus.OK).body("게시물 수정 성공");
    }

    // 게시물 삭제
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePost(
        @PathVariable("id") long postId,
        @AuthenticationPrincipal UserDetails userDetails) {
        postService.deletePost(postId, userDetails);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    // 좋아요
    @PatchMapping("/like/{id}")
    public ResponseEntity<Void> likePost(@PathVariable("id") long id,
        @AuthenticationPrincipal UserDetails userDetails) {
        postService.likePost(id, userDetails);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    // 검색
    @GetMapping("/search")
    public ResponseEntity<CursorResponseDto<PostResponseDto.SearchList>> getSearchList(
        @Valid PostRequestDto.Search dto,
        @RequestParam(required = false) LocalDateTime cursorUpdatedAt,
        @RequestParam(required = false) Long cursorId) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.getSearchList(dto, cursorUpdatedAt, cursorId));
    }


}
