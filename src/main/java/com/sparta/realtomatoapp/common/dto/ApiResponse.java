package com.sparta.realtomatoapp.common.dto;

import com.sparta.realtomatoapp.auth.dto.UserResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ApiResponse<T> {
    private String message;
    private List<T> data; //todo : 조금 더 범용적으로 바꿔야함. 현재는 유저 DTO 밖에 못가져옴
}
