package org.example.postory.domain.post.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.post.dto.PostRequestDto;
import org.example.postory.domain.post.dto.PostResponseDto.Get;
import org.example.postory.domain.post.dto.PostResponseDto.NewsFeed;
import org.example.postory.domain.post.entity.Post;
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
    public ResponseEntity<Get> getPostById(
        @PathVariable("id") long id, @AuthenticationPrincipal UserDetails userDetails) {
        // 사용자 ID 가져오기
        Long userId = userDetails != null ? Long.valueOf(userDetails.getUsername()) : null;
        Post post = postService.getPostById(id, userId); // 첫번째 매개변수 : @PathVariable 에서 온 게시물 id
        Get response = Get.fromPostEntity(post);
        return ResponseEntity.status(HttpStatus.OK).body(response);
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
    public ResponseEntity<Get> createPost(
        @Valid @RequestBody PostRequestDto.Create request,
        @AuthenticationPrincipal UserDetails userDetails) {
        Get response =  postService.createPost(request, userDetails);
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

    @PatchMapping("/like/{id}")
    public ResponseEntity<Void> likePost(@PathVariable("id") long id,
        @AuthenticationPrincipal UserDetails userDetails) {
        postService.likePost(id, userDetails);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 게시물 삭제
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePost(
        @PathVariable("id") long postId,
        @AuthenticationPrincipal UserDetails userDetails) {
        postService.deletePost(postId, userDetails);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
