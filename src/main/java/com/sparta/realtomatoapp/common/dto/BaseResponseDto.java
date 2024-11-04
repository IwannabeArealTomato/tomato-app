package com.sparta.realtomatoapp.common.dto;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Getter
@Setter
@Builder(builderMethodName = "baseResponseBuilder")
public class BaseResponseDto {
    private String message;
}
