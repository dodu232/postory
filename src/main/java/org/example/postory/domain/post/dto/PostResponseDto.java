package org.example.postory.domain.post.dto;

import lombok.Builder;
import lombok.Data;
import org.example.postory.domain.post.entity.Post;

@Data
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String hashtag;
    private int postLikeCount;
    private String writer;

    public static PostResponseDto fromPostEntity(Post post) {
        return PostResponseDto.builder()   // builder() : dto 객체를 직접 new 생성하지 않고 명시적으로 필드 지정해서 생성
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .hashtag(post.getHashtag())
                .postLikeCount(post.getPostLikeCount())
                .writer(post.getUser().getName())
                .build();  // build() : builder()를 바탕으로 실제 객체를 만듦
    }
}
