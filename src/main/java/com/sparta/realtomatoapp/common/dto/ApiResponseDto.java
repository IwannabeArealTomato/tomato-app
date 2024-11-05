package com.sparta.realtomatoapp.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ApiResponseDTO<T> {
    private String message;
    private List<T> data;
}
