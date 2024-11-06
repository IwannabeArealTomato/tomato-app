package com.sparta.realtomatoapp.security.config;

import com.sparta.realtomatoapp.user.dto.AuthUser;
import com.sparta.realtomatoapp.user.entity.UserRole;
import com.sparta.realtomatoapp.user.repository.UserRepository;
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
    public String createJwtToken(AuthUser authUser) {
        return Jwts.builder()
                .subject(authUser.getEmail())
                .claim("role", authUser.getRole())
                .claim("userId", authUser.getUserId())
                .expiration(Date.from(Instant.now().plus(jwtConfig.getJwtaccesstokenexpiretime(), ChronoUnit.MINUTES)))
                .issuedAt(Date.from(Instant.now()))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getJwtaccessTokenSecretKey().getBytes()), Jwts.SIG.HS256)
                .compact();
    }

    //JWT 토큰 검증
    public boolean verifyAccessToken(String jwtAccessToken) {
        SecretKey secureAccessSecret = Keys.hmacShaKeyFor(jwtConfig.getJwtaccessTokenSecretKey().getBytes());
        try {
            Jwts.parser().verifyWith(secureAccessSecret).build().parseSignedClaims(jwtAccessToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰으로 부터 정보 가져오기
    public AuthUser getCurrentRequestAuthInfo(String jwtAccessToken) {
        SecretKey secureAccessSecret = Keys.hmacShaKeyFor(jwtConfig.getJwtaccessTokenSecretKey().getBytes());
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(secureAccessSecret).build()
                .parseSignedClaims(jwtAccessToken);

        Claims payload = claimsJws.getPayload();
        String email = payload.getSubject();
        String role = payload.get("role", String.class);
        Long userId = payload.get("userId", Long.class);

        return AuthUser.builder()
                .email(email)
                .role(UserRole.valueOf(role))
                .userId(userId)
                .build();
    }

}
