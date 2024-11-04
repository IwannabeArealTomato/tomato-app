package com.sparta.realtomatoapp.common.dto;


public abstract class BaseApiResponseDto<T> {
    private final String message;

    public BaseApiResponseDto(String message) {
        this.message = message;
    }
}
