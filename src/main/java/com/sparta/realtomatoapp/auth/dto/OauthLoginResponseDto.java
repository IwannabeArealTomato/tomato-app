 package com.sparta.realtomatoapp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OauthLoginResponseDto {
    private String accessToken;
    private String refreshToken;
}
