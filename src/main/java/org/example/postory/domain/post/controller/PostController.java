package org.example.postory.domain.post.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.post.dto.PostRequestDto;
import org.example.postory.domain.post.dto.PostResponseDto.Get;
import org.example.postory.domain.post.dto.PostResponseDto.NewsFeed;
import org.example.postory.domain.post.entity.Post;
import org.example.postory.domain.post.service.PostService;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                @Valid @RequestBody PostRequestDto request,
                @AuthenticationPrincipal UserDetails userDetails) {
            Long userId = userDetails != null ? Long.valueOf(userDetails.getUsername()) : null;
            Post saved = postService.createPost(request, userId);
            Get response = Get.fromPostEntity(saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
      
        @PatchMapping("/like/{id}")
        public ResponseEntity<Void> likePost(@PathVariable("id") long id,
                @AuthenticationPrincipal UserDetails userDetails) {
                postService.likePost(id, userDetails);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }
