package org.example.postory.global.common.pagination;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CursorDto {

    private final LocalDateTime updatedAt;
    private final Long id;

    public CursorDto(LocalDateTime updatedAt, Long id) {
        this.updatedAt = updatedAt;
        this.id = id;
    }
}
