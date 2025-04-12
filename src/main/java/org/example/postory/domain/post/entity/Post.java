package org.example.postory.domain.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.example.postory.domain.post.dto.PostRequestDto;
import org.example.postory.domain.user.entity.User;
import org.example.postory.global.common.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private boolean isPostPublic;
    @Column(nullable = false)
    private String hashtag;
    @Column(nullable = false)
    private int postLikeCount = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Post(long id) {
        this.id = id;
    }

    public void updatePost(PostRequestDto.Update updatePost) {
        if (updatePost.getTitle() != null) {
            this.title = updatePost.getTitle();
        }
        if (updatePost.getContent() != null) {
            this.content = updatePost.getContent();
        }
        if (updatePost.getIsPostPublic() != null) { // Dto Boolean 으로 설정하기!
            this.isPostPublic = updatePost.getIsPostPublic();
        }
        if (updatePost.getHashtag() != null) {
            this.hashtag = updatePost.getHashtag();
        }
    }

    public void markAsDeleted() {  // soft delete 방식 : 삭제된 시간만 기록
        this.setDeletedAt(LocalDateTime.now());
    }

    public void upLikeCount() {
        this.postLikeCount++;
    }

    public void downLikeCount() {
        this.postLikeCount--;
    }
}
