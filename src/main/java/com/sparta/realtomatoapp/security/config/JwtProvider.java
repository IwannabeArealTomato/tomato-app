package com.sparta.realtomatoapp.security.config;

import com.sparta.realtomatoapp.auth.dto.AuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtConfig jwtConfig;

    //JWT 토큰 생성
    public String createJwtToken(AuthInfo authInfo) {
        return Jwts.builder()
                .subject(authInfo.getEmail())
                .claim("role", authInfo.getRole())
                .expiration(Date.from(Instant.now().plus(jwtConfig.getAccessTokenExpiration(), ChronoUnit.MINUTES)))
                .issuedAt(Date.from(Instant.now()))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getAccessTokenSecretKey().getBytes()), Jwts.SIG.HS256)
                .compact();
    }

    //JWT 토큰 검증
    public boolean verifyAccessToken(String accessToken) {
        SecretKey secureAccessSecret = Keys.hmacShaKeyFor(jwtConfig.getAccessTokenSecretKey().getBytes());
        try {
            Jwts.parser().verifyWith(secureAccessSecret).build().parseSignedClaims(accessToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰으로 부터 정보 가져오기
    public AuthInfo getCurrentRequestAuthInfo(String accessToken) {
        SecretKey secureAccessSecret = Keys.hmacShaKeyFor(jwtConfig.getAccessTokenSecretKey().getBytes());
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(secureAccessSecret).build()
                .parseSignedClaims(accessToken);

        Claims payload = claimsJws.getPayload();
        String email = payload.getSubject();
        String role = payload.get("role", String.class);

        return AuthInfo.builder()
                .email(email)
                .role(role)
                .build();
    }
}
