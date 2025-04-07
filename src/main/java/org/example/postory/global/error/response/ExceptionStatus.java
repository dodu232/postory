package org.example.postory.global.error.response;

public interface ExceptionStatus {
    int getCode();
    int getStatus();
    String getMessage();
}
