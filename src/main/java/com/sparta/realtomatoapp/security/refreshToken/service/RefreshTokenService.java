package com.sparta.realtomatoapp.security.refreshToken.service;

import com.sparta.realtomatoapp.security.refreshToken.entity.RefreshToken;
import com.sparta.realtomatoapp.security.refreshToken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // Refresh Token 생성 및 데이터베이스에 저장
    public void createAndStoreRefreshToken(String userEmail, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .userEmail(userEmail)
                .expiryDate(Instant.now().plusMillis(1440 * 60 * 1000)) // 토큰 만료 시간 설정
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
}
