package com.sparta.realtomatoapp.jwt.util;

import com.sparta.realtomatoapp.domain.user.common.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 5 * 60 * 1000L; // 5분
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 60 * 60 * 1000L; // 1시간

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void initializeKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String username, UserRoleEnum role) {
        return createToken(username, role, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public String createRefreshToken(String username, UserRoleEnum role) {
        return createToken(username, role, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    private String createToken(String username, UserRoleEnum role, long expirationTime) {
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.getAuthority())
                .setExpiration(expirationDate)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
