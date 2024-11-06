package com.sparta.realtomatoapp.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private final JwtConfig jwtConfig;
    private final Key accessTokenKey;
    private final Key refreshTokenKey;

    public JwtProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        // HS512에 적합한 512비트 이상의 시크릿 키 자동 생성
        this.accessTokenKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        this.refreshTokenKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateToken(String email) {
        return createToken(email, jwtConfig.getJwtAccessTokenExpireTime(), accessTokenKey);
    }

    public String generateRefreshToken(String email) {
        return createToken(email, jwtConfig.getJwtRefreshTokenExpireTime(), refreshTokenKey);
    }

    private String createToken(String email, long expireTime, Key key) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireTime * 60 * 1000);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key) // 자동 생성된 키 사용
                .compact();
    }

    // Access Token 유효성 검사
    public boolean verifyAccessToken(String token) {
        return verifyToken(token, accessTokenKey);
    }

    // Refresh Token 유효성 검사
    public boolean verifyRefreshToken(String token) {
        return verifyToken(token, refreshTokenKey);
    }

    // 토큰 유효성 검증 메서드 (공통 로직)
    private boolean verifyToken(String token, Key key) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            log.error("Token verification failed: {}", ex.getMessage());
            return false;
        }
    }

    // Access Token에서 사용자 이름 추출
    public String getUserFromToken(String token) {
        return getUserFromToken(token, accessTokenKey);
    }

    // Refresh Token에서 사용자 이름 추출
    public String getUserFromRefreshToken(String token) {
        return getUserFromToken(token, refreshTokenKey);
    }

    // 사용자 이름 추출 메서드 (공통 로직)
    private String getUserFromToken(String token, Key key) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
