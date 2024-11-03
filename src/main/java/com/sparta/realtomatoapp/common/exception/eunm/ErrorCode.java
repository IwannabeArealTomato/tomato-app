package com.sparta.realtomatoapp.common.exception.eunm;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found", 404),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token", 401),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied", 403);

    private final HttpStatus httpStatus;
    private final String message;
    private final int status;

    ErrorCode(HttpStatus httpStatus, String message, int status) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.status = status;
    }
}
