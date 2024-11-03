package com.sparta.realtomatoapp.auth.config;

public class JwtConfig {
    //토큰의 만료 시간(5분)
    private Integer accessTokenExpiration = 5;

    // 서명 키(secret key),
    private String accessTokenSecretKey = "access_token_secret_access_token_secret_access_token_secret";
}
