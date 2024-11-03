package com.sparta.realtomatoapp.auth.config;

import com.sparta.realtomatoapp.auth.dto.AuthInfo;
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

    //JWT 토큰 생성
    public String creatJwtToken(AuthInfo authInfo) {
        return Jwts.builder()
                .setSubject(authInfo.getEmail())
                .claim("role", authInfo.getRole())
                .setExpiration(Date.from(Instant.now().plus(jwtConfig.getAccessTokenExpiration(), ChronoUnit.MINUTES)))
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getAccessTokenSecretKey().getBytes()))
                .compact();
    }

    //JWT 토큰 검증
    public boolean verifyAccessToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtConfig.getAccessTokenSecretKey())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
