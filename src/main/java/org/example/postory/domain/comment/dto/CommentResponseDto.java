package org.example.postory.domain.comment.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.comment.entity.Comment;

public class CommentResponseDto {

    @Getter
    @RequiredArgsConstructor
    public static class CommentItem{
        private final Long id;
        private final String content;
        private final int commentLikeCount;
        private final String writer;
        private final LocalDateTime createdAt;

        public CommentItem(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.commentLikeCount = comment.getCommentLikeCount();
            this.writer = comment.getUser().getName();
            this.createdAt = comment.getCreatedAt();
        }
    }


}
