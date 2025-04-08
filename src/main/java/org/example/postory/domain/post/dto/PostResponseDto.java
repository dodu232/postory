package org.example.postory.domain.post.dto;

import java.time.LocalDateTime;
import org.example.postory.domain.post.entity.Post;

public class PostResponseDto {
    private final Long id;

    private final String title;

    private final String content;

    private final boolean isPublic;

    private final String hashtag;

    private final int postLikeCount;

    private final Long userId;

    private final LocalDateTime createAt;

    private final boolean isUpdated;

    public PostResponseDto(Long id, String title, String content, boolean isPublic, String hashtag,
        int postLikeCount, Long userId, LocalDateTime createAt, boolean isUpdated) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.hashtag = hashtag;
        this.postLikeCount = postLikeCount;
        this.userId = userId;
        this.createAt = createAt;
        this.isUpdated = isUpdated;
    }

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isPublic = post.isPublic();
        this.hashtag = post.getHashtag();
        this.postLikeCount = post.getPostLikeCount();
        this.userId = post.getUser().getId();
        this.createAt = post.getCreatedAt();
        this.isUpdated = !post.getCreatedAt().isEqual(post.getUpdatedAt());

    }
}
