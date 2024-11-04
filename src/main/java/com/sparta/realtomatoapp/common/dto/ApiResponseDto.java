package com.sparta.realtomatoapp.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApiResponseDto<T> {
    private String message;
    private List<T> data;

    public ApiResponseDto(String message, List<T> data) {
        this.message = message;
        this.data = data;
    }
}
