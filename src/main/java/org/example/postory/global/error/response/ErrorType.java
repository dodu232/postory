package org.example.postory.global.error.response;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorType implements ExceptionStatus {
    /**
     * 1000: 요청 성공 (OK)
     */
    SUCCESS(1000, HttpStatus.OK.value(), "요청에 성공하였습니다."),

    /**
     * 2000: 요청 실패
     */
    ARGUMENT_TYPE_MISMATCH(2001, HttpStatus.BAD_REQUEST.value(), "잘못된 파라미터 타입입니다.");

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
