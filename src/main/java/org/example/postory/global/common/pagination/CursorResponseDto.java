package org.example.postory.global.common.pagination;

import java.util.List;
import lombok.Getter;

@Getter
public class CursorResponseDto<T> {

    private final List<T> data;
    private final CursorDto nextCursor;

    public CursorResponseDto(List<T> data, CursorDto nextCursor) {
        this.data = data;
        this.nextCursor = nextCursor;
    }

    public static <T> CursorResponseDto<T> of(List<T> data, CursorDto nextCursor) {
        return new CursorResponseDto<>(data, nextCursor);
    }

}
