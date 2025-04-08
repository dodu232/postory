package org.example.postory.global.error.response;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorType implements ExceptionStatus {
    /**
     * TODO: 도메인 별 에러타입 추가 필요
     */
    ;

    private final int code;
    private final int status;
    private final String message;

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
