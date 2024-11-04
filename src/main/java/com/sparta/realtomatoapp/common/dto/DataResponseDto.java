package com.sparta.realtomatoapp.common.dto;

import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DataResponseDto<T> extends BaseResponseDto {
    private List<T> data;

    @Builder(builderMethodName = "dataResponseBuilder")
    public DataResponseDto(String message, List<T> data) {
        super(message);
        this.data = data;
    }
}
