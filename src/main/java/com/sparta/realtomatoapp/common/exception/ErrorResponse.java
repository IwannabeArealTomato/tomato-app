package com.sparta.realtomatoapp.common.exception;

import com.sparta.realtomatoapp.common.exception.eunm.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String message;
    private final int status;

    public ErrorResponse(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
    }
}
