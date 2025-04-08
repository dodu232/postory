package org.example.postory.global.error.response;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorType implements ExceptionStatus {
    /**
     * User 도메인 에러 타입
     */
    DUPLICATE_EMAIL(2001, HttpStatus.CONFLICT.value(), "이미 존재하는 이메일입니다."),
    DUPLICATE_PHONE(2002, HttpStatus.CONFLICT.value(), "이미 존재하는 전화번호입니다.");

    private final int code;
    private final int status;
    private final String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
