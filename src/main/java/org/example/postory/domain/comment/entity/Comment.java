package org.example.postory.domain.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.postory.domain.post.entity.Post;
import org.example.postory.domain.user.entity.User;
import org.example.postory.global.common.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Setter
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private int commentLikeCount = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }

    public Comment(Long id) {
        this.id = id;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void markAsDeleted() { // soft delete
        this.setDeletedAt(LocalDateTime.now());
    }

    public void upLikeCount() {
        this.commentLikeCount++;
    }

    public void downLikeCount() {
        this.commentLikeCount--;
    }
}
