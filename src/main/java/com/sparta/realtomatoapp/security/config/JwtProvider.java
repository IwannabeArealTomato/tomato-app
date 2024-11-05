package com.sparta.realtomatoapp.security.config;

import com.sparta.realtomatoapp.auth.dto.AuthInfo;
import com.sparta.realtomatoapp.user.entity.User;
import com.sparta.realtomatoapp.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;

    //JWT 토큰 생성
    public String createJwtToken(AuthInfo authInfo) {
        return Jwts.builder()
                .subject(authInfo.getEmail())
                .claim("role", authInfo.getRole())
                .claim("userId", authInfo.getUserId())
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
    public AuthInfo getCurrentRequestAuthInfo(String jwtAccessToken) {
        SecretKey secureAccessSecret = Keys.hmacShaKeyFor(jwtConfig.getJwtaccessTokenSecretKey().getBytes());
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(secureAccessSecret).build()
                .parseSignedClaims(jwtAccessToken);

        Claims payload = claimsJws.getPayload();
        String email = payload.getSubject();
        String role = payload.get("role", String.class);
        String userId = payload.get("userId", String.class);

        return AuthInfo.builder()
                .email(email)
                .role(role)
                .userId(userId)
                .build();
    }

}
