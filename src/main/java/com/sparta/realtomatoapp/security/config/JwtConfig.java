package com.sparta.realtomatoapp.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtConfig {

    // Access Token 만료 시간 (분 단위)
    @Value("${access.token.expire.time}")
    private long accessTokenExpireTime;

    // Access Token 시크릿 키
    @Value("${access.token.secret.key}")
    private String accessTokenSecretKey;

    // Refresh Token 만료 시간 (분 단위)
    @Value("${refresh.token.expire.time}")
    private long refreshTokenExpireTime;

    // Refresh Token 시크릿 키
    @Value("${refresh.token.secret.key}")
    private String refreshTokenSecretKey;
}
