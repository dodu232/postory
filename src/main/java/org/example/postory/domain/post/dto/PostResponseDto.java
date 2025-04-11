package org.example.postory.domain.post.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.post.entity.Post;

public class PostResponseDto {

    @Getter
    @RequiredArgsConstructor
    public static class NewsFeed {

        private final Long id;
        private final String title;
        private final String content;
        private final boolean isPostPublic;
        private final String hashtag;
        private final int postLikeCount;
        private final String writer;
        private final LocalDateTime createAt;
        private final boolean isUpdated;

        public NewsFeed(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.isPostPublic = post.isPostPublic();
            this.hashtag = post.getHashtag();
            this.postLikeCount = post.getPostLikeCount();
            this.writer = post.getUser().getName();
            this.createAt = post.getCreatedAt();
            this.isUpdated = !post.getCreatedAt().isEqual(post.getUpdatedAt());
        }
    }

    @Getter
    @Builder
    public static class Get {

        private Long id;
        private String title;
        private String content;
        private String hashtag;
        private int postLikeCount;
        private boolean isUpdated;
        private String writer;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private boolean isPostPublic;



        public static Get fromPostEntity(Post post) {
            return Get.builder()   // builder() : dto 객체를 직접 new 생성하지 않고 명시적으로 필드 지정해서 생성
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .hashtag(post.getHashtag())
                .postLikeCount(post.getPostLikeCount())
                .isPostPublic(post.isPostPublic())
                .isUpdated(!post.getCreatedAt().isEqual(post.getUpdatedAt()))
                .writer(post.getUser().getName())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();  // build() : builder()를 바탕으로 실제 객체를 만듦
        }
    }

    @AllArgsConstructor
    @Getter
    public static class SearchList {

        private long id;
        private String title;
        private String name;
        private LocalDateTime updatedAt;
    }
}
