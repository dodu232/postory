package org.example.postory.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.post.enums.SearchType;

@Getter
@RequiredArgsConstructor
public class PostRequestDto {

    @Getter
    public static class Create {

        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
        private String title;
        @NotBlank(message = "내용은 필수 입력 항목입니다.")
        @Size(max = 500, message = "내용은 최대 500자까지 입력 가능합니다.")
        private String content;
        @JsonProperty("isPostPublic")  // isPostPublic을 PostPublic으로 추론하는 에러에 대한 해결코드
        private boolean isPostPublic;
        @NotBlank(message = "해시태그는 필수 입력 항목입니다.")
        @Size(max = 100, message = "해시태그는 최대 100자까지 입력 가능합니다.")
        private String hashtag;
    }

    @Getter
    public static class Update {

        @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
        @Pattern(regexp = "^(?!\\s*$).+", message = "수정할 내용을 입력해주세요.")
        private String title;
        @Size(max = 500, message = "내용은 최대 500자까지 입력 가능합니다.")
        @Pattern(regexp = "^(?!\\s*$).+", message = "수정할 내용을 입력해주세요.")
        private String content;
        @JsonProperty("isPostPublic")
        private Boolean isPostPublic;
        @Size(max = 100, message = "해시태그는 최대 100자까지 입력 가능합니다.")
        @Pattern(regexp = "^(?!\\s*$).+", message = "수정할 내용을 입력해주세요.")
        private String hashtag;
    }

    @AllArgsConstructor
    @Getter
    public static class Search {

        @NotNull(message = "searchType은 필수 입력 항목입니다.")
        private SearchType searchType;

        @NotBlank(message = "value는 필수 입력 항목입니다.")
        private String value;

    }
}
