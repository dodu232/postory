package org.example.postory.domain.comment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CommentRequestDto {

    @Getter
    @AllArgsConstructor
    public static class CommentItem{
        @NotBlank(message = "댓글의 내용을 작성해주세요.")
        @Size( max = 500, message = "500자 미만으로 댓글을 작성해주세요")
        private String contents;
    }
}
