package org.example.postory.global.common.pagination;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CursorDto {

    private LocalDateTime updatedAt;
    private Long id;

    public CursorDto(LocalDateTime updatedAt, Long id) {
        this.updatedAt = updatedAt;
        this.id = id;
    }

    public CursorDto(Long id) {
        this.id = id;
    }
}
