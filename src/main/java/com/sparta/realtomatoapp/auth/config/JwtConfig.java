package com.sparta.realtomatoapp.auth.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class JwtConfig {

    //토큰의 만료 시간(5분)
    private final Integer accessTokenExpiration = 5;

    // 서명 키(secret key),
    private final String accessTokenSecretKey = "access_token_secret_access_token_secret_access_token_secret";
}
