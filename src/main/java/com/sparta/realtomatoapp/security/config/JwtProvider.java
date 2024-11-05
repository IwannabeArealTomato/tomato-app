package com.sparta.realtomatoapp.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    public String generateAccessToken(String username) {
        return createToken(username, jwtConfig.getAccessTokenExpireTime(), accessTokenKey);
    }

    public String generateRefreshToken(String username) {
        return createToken(username, jwtConfig.getRefreshTokenExpireTime(), refreshTokenKey);
    }

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

    // 추가된 메서드: 토큰의 Claims 반환
    public Claims getClaimsFromToken(String token, boolean isAccessToken) {
        Key key = isAccessToken ? accessTokenKey : refreshTokenKey;
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
