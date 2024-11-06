package com.sparta.realtomatoapp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginTokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
