package com.sparta.realtomatoapp.security.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class JwtConfig {

    // Access Token 만료 시간 (분 단위)
    @Value("${access.token.expire.time}")
    private long accessTokenExpireTime;

    // Refresh Token 만료 시간 (분 단위)
    @Value("${refresh.token.expire.time}")
    private long refreshTokenExpireTime;
}
