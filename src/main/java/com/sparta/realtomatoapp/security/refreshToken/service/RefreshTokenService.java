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

    public void createAndStoreRefreshToken(String userEmail, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .userEmail(userEmail)
                .expiryDate(Instant.now().plusMillis(1440 * 60 * 1000))
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    public boolean isTokenValid(String token) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(token);
        return refreshTokenOpt.isPresent() && refreshTokenOpt.get().getExpiryDate().isAfter(Instant.now());
    }

    public void invalidateRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public Optional<RefreshToken> findTokenByUserEmail(String userEmail) {
        return refreshTokenRepository.findByUserEmail(userEmail);
    }
}
