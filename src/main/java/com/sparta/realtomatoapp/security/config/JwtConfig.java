package com.sparta.realtomatoapp.security.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class JwtConfig {

    // 엑세스 토큰의 만료 시간 (분 단위)
    @Value("${access.token.expire.time}")
    private Integer jwtAccessTokenExpireTime;

    // 엑세스 토큰 서명 키 (secret key)
    @Value("${access.token.secret.key}")
    private String jwtAccessTokenSecretKey;

    // 리프레쉬 토큰의 만료 시간 (분 단위)
    @Value("${refresh.token.expire.time}")
    private Integer jwtRefreshTokenExpireTime;

    // 리프레쉬 토큰 서명 키 (secret key)
    @Value("${refresh.token.secret.key}")
    private String jwtRefreshTokenSecretKey;
}
