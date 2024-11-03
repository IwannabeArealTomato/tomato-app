package com.sparta.realtomatoapp.auth.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtConfig jwtConfig;

    //JWT 생성 및 검증을 담당
    public String creatJwtToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setExpiration(Date.from(Instant.now().plus(jwtConfig.getAccessTokenExpiration(), ChronoUnit.MINUTES)))
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getAccessTokenSecretKey().getBytes()))
                .compact();
    }

}
