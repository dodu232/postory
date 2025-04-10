package org.example.postory.domain.comment.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CommentRequestDto {

    @Getter
    @AllArgsConstructor
    public static class Create{
        private String contents;
        private Long postId;
    }
}
