package com.sparta.realtomatoapp.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDTO {
    private String accessToken;
    private String refreshToken;
}
