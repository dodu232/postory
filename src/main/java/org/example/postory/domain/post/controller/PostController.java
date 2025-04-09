package org.example.postory.domain.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.post.dto.PostResponseDto;
import org.example.postory.domain.post.dto.PostResponseDto.ProfileInquiry;
import org.example.postory.domain.post.dto.PostResponseDto.SingleQuery;
import org.example.postory.domain.post.entity.Post;
import org.example.postory.domain.post.service.PostService;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<SingleQuery> getPostById(
            @PathVariable("id") long id, HttpServletRequest request) {

        // 나중에 session 설정할 때 해당 userId 코드 수정할수도.
        Long userId = request.getSession().getAttribute("user_id") != null
                ? Long.valueOf(request.getSession().getAttribute("user_id").toString())
                : null;

        Post post = postService.getPostById(id, userId); // 첫번째 매개변수 : @PathVariable 에서 온 게시물 id
        return ResponseEntity.ok(PostResponseDto.SingleQuery.fromPostEntity(post));
    }

    // 뉴스피드 조회
    @GetMapping
    public ResponseEntity<CursorResponseDto<ProfileInquiry>> getNewsFeed(
        @RequestParam(required = false)LocalDateTime cursorUpdatedAt,
        @RequestParam(required = false)Long cursorId
    ) {
        CursorResponseDto<ProfileInquiry> newsFeedResponse = postService.getNewsFeed(cursorUpdatedAt, cursorId);
        return ResponseEntity.ok(newsFeedResponse);
    }

}
