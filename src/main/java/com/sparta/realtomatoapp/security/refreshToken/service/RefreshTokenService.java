package com.sparta.realtomatoapp.security.refreshToken.service;

import com.sparta.realtomatoapp.security.config.JwtConfig;
import com.sparta.realtomatoapp.security.refreshToken.entity.RefreshToken;
import com.sparta.realtomatoapp.security.refreshToken.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtConfig jwtConfig;
    private static final long EXPIRING_SOON_THRESHOLD = 24 * 60 * 60 * 1000; // 1일 (밀리초 단위)
    private final RefreshTokenRepository refreshTokenRepository;

    // Refresh Token 생성 및 데이터베이스에 저장
    public void createAndStoreRefreshToken(String userEmail, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .userEmail(userEmail)
                .expiryDate(Instant.now().plusMillis(jwtConfig.getJwtRefreshTokenExpireTime() * 60 * 1000))
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    // Refresh Token 유효성 검사
    public boolean isTokenValid(String token) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(token);
        return refreshTokenOpt.isPresent() && refreshTokenOpt.get().getExpiryDate().isAfter(Instant.now());
    }

    // Refresh Token 무효화
    public void invalidateRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    // 사용자 이메일로 Refresh Token 조회
    public Optional<RefreshToken> findTokenByUserEmail(String userEmail) {
        return refreshTokenRepository.findByUserEmail(userEmail);
    }

    // 만료 임박 시 새 Refresh Token 발급 필요 여부 확인
    public boolean isTokenExpiringSoon(String refreshToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtConfig.getJwtRefreshTokenSecretKey().getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

        Date expirationDate = claims.getExpiration();
        long timeRemaining = expirationDate.getTime() - System.currentTimeMillis();

        return timeRemaining < EXPIRING_SOON_THRESHOLD;
    }
}
