package com.sparta.realtomatoapp.security.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private final JwtConfig jwtConfig;
    private final Key accessTokenKey;
    private final Key refreshTokenKey;

    public JwtProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.accessTokenKey = Keys.hmacShaKeyFor(jwtConfig.getAccessTokenSecretKey().getBytes(StandardCharsets.UTF_8));
        this.refreshTokenKey = Keys.hmacShaKeyFor(jwtConfig.getRefreshTokenSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    // Access Token 생성 메서드
    public String generateAccessToken(String username) {
        return createToken(username, jwtConfig.getAccessTokenExpireTime(), accessTokenKey);
    }

    // Refresh Token 생성 메서드
    public String generateRefreshToken(String username) {
        return createToken(username, jwtConfig.getRefreshTokenExpireTime(), refreshTokenKey);
    }

    // 공통 토큰 생성 메서드
    private String createToken(String username, long expireTime, Key key) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireTime * 60 * 1000);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public boolean verifyAccessToken(String token) {
        return verifyToken(token, accessTokenKey);
    }

    public boolean verifyRefreshToken(String token) {
        return verifyToken(token, refreshTokenKey);
    }

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

    public String getUserFromToken(String token) {
        return getUserFromToken(token, accessTokenKey);
    }

    public String getUserFromRefreshToken(String token) {
        return getUserFromToken(token, refreshTokenKey);
    }

    private String getUserFromToken(String token, Key key) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Claims getClaimsFromToken(String token, boolean validateExpiration) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(accessTokenKey) // JWT의 키를 설정
                    .build()
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();

            // 만료 여부를 검사하지 않도록 설정된 경우, 만료된 Claims도 반환
            if (!validateExpiration || claims.getExpiration().after(new Date())) {
                return claims;
            } else {
                throw new JwtException("토큰이 만료되었습니다.");
            }
        } catch (JwtException e) {
            log.error("Failed to parse token or token is expired: {}", e.getMessage());
            return null;
        }
    }
}
