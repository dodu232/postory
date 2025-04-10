package org.example.postory.domain.comment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CommentRequestDto {

    @Getter
    @AllArgsConstructor
    public static class Create{
        @NotBlank(message = "댓글의 내용을 작성해주세요.")
        private String contents;
    }
}
