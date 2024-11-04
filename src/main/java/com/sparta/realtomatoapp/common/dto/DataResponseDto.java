package com.sparta.realtomatoapp.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public abstract class DataResponseDto<T> extends BaseApiResponseDto<T> {
    private List<T> data;

    public DataResponseDto(String message, List<T> data) {
        super(message);
        this.data = data;
    }
}
