package org.example.postory.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowingResponseDto {
    private Long userId;
    private String name;
}
