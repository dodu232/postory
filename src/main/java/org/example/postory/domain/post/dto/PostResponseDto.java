package org.example.postory.domain.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.example.postory.domain.post.entity.Post;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

public class PostResponseDto {

    @Getter
    @RequiredArgsConstructor
    public static class NewsFeed{
        private final Long id;
        private final String title;
        private final String content;
        private final boolean isPublic;
        private final String hashtag;
        private final int postLikeCount;
        private final String writer;
        private final LocalDateTime createAt;
        private final boolean isUpdated;

        public NewsFeed(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.isPublic = post.isPublic();
            this.hashtag = post.getHashtag();
            this.postLikeCount = post.getPostLikeCount();
            this.writer = post.getUser().getName();
            this.createAt = post.getCreatedAt();
            this.isUpdated = !post.getCreatedAt().isEqual(post.getUpdatedAt());
        }
    }

    @Data
    @Builder
    public static class SingleQuery{
        private Long id;
        private String title;
        private String content;
        private String hashtag;
        private int postLikeCount;
        private String writer;

        public static SingleQuery fromPostEntity(Post post) {
            return SingleQuery.builder()   // builder() : dto 객체를 직접 new 생성하지 않고 명시적으로 필드 지정해서 생성
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .hashtag(post.getHashtag())
                .postLikeCount(post.getPostLikeCount())
                .writer(post.getUser().getName())
                .build();  // build() : builder()를 바탕으로 실제 객체를 만듦
        }
    }
}
