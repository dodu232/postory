package org.example.postory.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class ApiException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final ErrorType errorType;
    private final String message;
}
