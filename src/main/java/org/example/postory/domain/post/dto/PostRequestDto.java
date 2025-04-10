package org.example.postory.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class PostRequestDto {

    @Getter
    public static class Create {

        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
        private String title;
        @NotBlank(message = "내용은 필수 입력 항목입니다.")
        @Size(max = 500, message = "내용은 최대 500자까지 입력 가능합니다.")
        private String content;
        private boolean isPublic;
        @NotBlank(message = "해시태그는 필수 입력 항목입니다.")
        @Size(max = 100, message = "해시태그는 최대 100자까지 입력 가능합니다.")
        private String hashtag;
    }

    @Getter
    public static class Update {

        @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
        private String title;
        @Size(max = 500, message = "내용은 최대 500자까지 입력 가능합니다.")
        private String content;
        private Boolean isPublic;
        @Size(max = 100, message = "해시태그는 최대 100자까지 입력 가능합니다.")
        private String hashtag;
    }


}