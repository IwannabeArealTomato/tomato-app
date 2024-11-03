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

    // 토큰의 만료 시간 (분 단위)
    @Value("${acess.token.expire.time}")
    private Integer accessTokenExpireTime;

    // 서명 키 (secret key)
    @Value("${acess.token.secret.key}")
    private String accessTokenSecretKey;
}
