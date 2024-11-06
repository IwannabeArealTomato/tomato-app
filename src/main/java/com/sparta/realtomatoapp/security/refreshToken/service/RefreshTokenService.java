package com.sparta.realtomatoapp.security.refreshToken.service;

import com.sparta.realtomatoapp.security.config.JwtProvider;
import com.sparta.realtomatoapp.security.refreshToken.entity.RefreshToken;
import com.sparta.realtomatoapp.security.refreshToken.repository.RefreshTokenRepository;
import com.sparta.realtomatoapp.user.dto.AuthUser;
import com.sparta.realtomatoapp.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    public String reissueAccessToken(String refreshToken) {
        /*if (!isTokenValid(refreshToken)) {
            throw new IllegalArgumentException("없는 리프레시 토큰이거나, 저장된 토큰이 유효하지 않습니다.");
        }*/
        RefreshToken savedtoken = refreshTokenRepository.findByTokenValue(refreshToken).orElseThrow(
                () -> new IllegalArgumentException("토큰이 존재하지 않습니다.")
        );
        if (!jwtProvider.verifyRefreshToken(savedtoken.getTokenValue())) {
            refreshTokenRepository.deleteByTokenValue(refreshToken);
            throw new IllegalArgumentException("저장된 리프레시 토큰이 유효하지 않습니다. 새로 로그인해주세요.");
        }

        User savedUser = savedtoken.getUser();
        AuthUser authUser = AuthUser.builder()
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .userId(savedUser.getUserId())
                .build();
        return jwtProvider.createJwtToken(authUser);
    }
}
