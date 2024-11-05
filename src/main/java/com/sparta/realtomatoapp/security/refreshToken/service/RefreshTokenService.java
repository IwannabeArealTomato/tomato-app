package com.sparta.realtomatoapp.security.refreshToken.service;

import com.sparta.realtomatoapp.security.refreshToken.entity.RefreshToken;
import com.sparta.realtomatoapp.security.refreshToken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // Refresh Token 생성
    public RefreshToken createRefreshToken(String userEmail, long expiryDuration) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .userEmail(userEmail)
                .expiryDate(Instant.now().plusMillis(expiryDuration))
                .build();

        return refreshTokenRepository.save(refreshToken);
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
        return refreshTokenRepository.findByToken(userEmail);
    }
}
