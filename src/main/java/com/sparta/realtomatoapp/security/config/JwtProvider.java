package com.sparta.realtomatoapp.security.config;

import com.sparta.realtomatoapp.user.dto.AuthUser;
import com.sparta.realtomatoapp.user.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    //JWT 엑세스 토큰 생성
    public String createJwtToken(AuthUser authUser) {
        return Jwts.builder()
                .subject(authUser.getEmail())
                .claim("role", authUser.getRole())
                .claim("userId", authUser.getUserId())
                .expiration(Date.from(Instant.now().plus(jwtConfig.getJwtAccessTokenExpireTime(), ChronoUnit.MINUTES)))
                .issuedAt(Date.from(Instant.now()))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getJwtAccessTokenSecretKey().getBytes()), Jwts.SIG.HS256)
                .compact();
    }

    //JWT 엑세스 토큰을 헤더에 담는 메서드
    public void addAccessTokenToHeader(HttpServletResponse response, String accessToken) {
        response.addHeader("Authorization", "Bearer " + accessToken);
    }

    //JWT 리프레시 토큰 생성
    public String createRefreshToken(AuthUser authUser) {
        return Jwts.builder()
                .subject(authUser.getUserId().toString())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(jwtConfig.getJwtRefreshTokenExpireTime(), ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getJwtRefreshSecretKey().getBytes()), Jwts.SIG.HS256)
                .compact();
    }

    //JWT 리프레시 토큰을 쿠키에 담기
    // 리프레시 토큰을 쿠키에 담아 응답으로 보내는 메서드
    public void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
//        refreshTokenCookie.setHttpOnly(true);  // JavaScript에서 접근할 수 없게 설정
//        refreshTokenCookie.setSecure(true);    // HTTPS를 통해서만 전송되도록 설정
        refreshTokenCookie.setPath("/");       // 쿠키의 유효 범위 설정
        refreshTokenCookie.setMaxAge(jwtConfig.getJwtRefreshTokenExpireTime());  // 쿠키 만료 시간 = 리프레시 토큰 만료 기간

        response.addCookie(refreshTokenCookie);  // 응답에 쿠키 추가
    }


    //JWT 토큰 검증
    // Access Token 유효성 검사
    public boolean verifyAccessToken(String jwtAccessToken) {
        SecretKey accessTokenKey = Keys.hmacShaKeyFor(jwtConfig.getJwtAccessTokenSecretKey().getBytes());
        return verifyToken(jwtAccessToken, accessTokenKey);
    }

    // Refresh Token 유효성 검사
    public boolean verifyRefreshToken(String jwtRefreshToken) {
        SecretKey refreshTokenKey = Keys.hmacShaKeyFor(jwtConfig.getJwtRefreshSecretKey().getBytes());
        return verifyToken(jwtRefreshToken, refreshTokenKey);
    }

    private boolean verifyToken(String jwtAccessToken, SecretKey secretKey) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwtAccessToken);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT 형식 오류 or 서명 검증 실패 등 예외 발생", e);
            return false;
        }
    }

    // 토큰으로 부터 정보 가져오기
    public AuthUser getCurrentRequestAuthInfo(String jwtAccessToken) {
        SecretKey secureAccessSecret = Keys.hmacShaKeyFor(jwtConfig.getJwtAccessTokenSecretKey().getBytes());
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
