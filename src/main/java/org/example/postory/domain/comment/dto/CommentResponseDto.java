package org.example.postory.domain.comment.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.comment.entity.Comment;

public class CommentResponseDto {

    @Getter
    @RequiredArgsConstructor
    public static class Create{
        private final Long id;
        private final String contents;
        private final int commentLikeCount;
        private final LocalDateTime createAt;
        private final String username;

        public Create(Comment comment){
            this.id = comment.getId();
            this.contents = comment.getContent();
            this.commentLikeCount = comment.getCommentLikeCount();
            this.createAt = comment.getCreatedAt();
            this.username = comment.getUser().getName() == null ? "이름 없음": comment.getUser().getName();
        }
    }
}
