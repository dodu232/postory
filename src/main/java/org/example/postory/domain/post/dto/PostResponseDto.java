package org.example.postory.domain.post.dto;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.post.entity.Post;

@RequiredArgsConstructor
public class PostResponseDto {
    private final Long id;

    private final String title;

    private final String content;

    private final boolean isPublic;

    private final String hashtag;

    private final int postLikeCount;

    private final String username;

    private final LocalDateTime createAt;

    private final boolean isUpdated;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isPublic = post.isPublic();
        this.hashtag = post.getHashtag();
        this.postLikeCount = post.getPostLikeCount();
        this.username = post.getUser().getName();
        this.createAt = post.getCreatedAt();
        this.isUpdated = !post.getCreatedAt().isEqual(post.getUpdatedAt());
    }
}
