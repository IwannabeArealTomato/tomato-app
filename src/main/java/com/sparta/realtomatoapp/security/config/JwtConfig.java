package com.sparta.realtomatoapp.security.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class JwtConfig {

    // 액세스 토큰의 만료 시간 (분 단위)
    @Value("${access.token.expire.time}")
    private Integer jwtAccessTokenExpireTime;

    // 액세스 토큰의 서명 키 (secret key)
    @Value("${access.token.secret.key}")
    private String jwtAccessTokenSecretKey;


    // 리프레시 토큰의 만료 시간 (분 단위)
    @Value("${refresh.token.expire.time}")
    private Integer jwtRefreshTokenExpireTime;

    // 리프레시 토큰의 서명 키 (secret key)
    @Value("${refresh.token.secret.key}")
    private String jwtRefreshSecretKey;
}
