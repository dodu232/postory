package org.example.postory.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequestDto {

    @NotBlank
    @Size(max = 100)
    private String title;
    @NotBlank
    @Size(max = 500)
    private String content;
    private boolean isPublic;
    @NotBlank
    @Size(max = 100)
    private String hashtag;
}
