package org.example.postory.global.error;

import lombok.Getter;
import org.example.postory.global.error.response.ExceptionStatus;

@Getter
public class ApiException extends RuntimeException {
    private final ExceptionStatus exceptionStatus;

    public ApiException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
