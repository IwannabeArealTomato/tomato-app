package com.sparta.realtomatoapp.jwt.util;

import com.sparta.realtomatoapp.domain.user.common.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization"; // 액세스 토큰을 담는 헤더 이름
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken"; // 리프레시 토큰을 담는 헤더 이름
    public static final String BEARER_PREFIX = "Bearer "; // 토큰 앞에 붙는 접두사

    private final long ACCESS_TOKEN_EXPIRATION_TIME = 5 * 60 * 1000L; // 액세스 토큰 만료 시간 (5분)
    private final long REFRESH_TOKEN_EXPIRATION_TIME = 60 * 60 * 1000L; // 리프레시 토큰 만료 시간 (1시간)

    @Value("${jwt.secret.key}")
    private String secretKey; // JWT 서명을 위한 비밀 키
    private Key key; // HMAC SHA 키 객체
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 서명 알고리즘 설정

    @PostConstruct
    public void initializeSecretKey() { // 비밀 키 초기화 메서드
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String username, UserRoleEnum role, boolean isRefreshToken) { // JWT 생성 메서드
        long expirationTime = isRefreshToken ? REFRESH_TOKEN_EXPIRATION_TIME : ACCESS_TOKEN_EXPIRATION_TIME;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.getAuthority())
                .setExpiration(expirationDate)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String extractTokenFromRequest(HttpServletRequest request) { // 요청 헤더에서 토큰 추출 메서드
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public String extractUsernameFromToken(String token) { // 토큰에서 사용자 이름 추출 메서드
        return getClaims(token).getSubject();
    }

    public String rotateRefreshToken(String oldRefreshToken) { // 리프레시 토큰 갱신 메서드
        if (isValidToken(oldRefreshToken)) {
            Claims claims = getClaims(oldRefreshToken);
            String username = claims.getSubject();
            UserRoleEnum role = UserRoleEnum.valueOf(claims.get("role").toString());
            return createToken(username, role, true);
        }
        throw new JwtException("Invalid Refresh Token");
    }

    public Claims getClaims(String token) { // 토큰에서 클레임(Claims) 추출 메서드
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean isValidToken(String token) { // 토큰의 유효성 검증 메서드
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
