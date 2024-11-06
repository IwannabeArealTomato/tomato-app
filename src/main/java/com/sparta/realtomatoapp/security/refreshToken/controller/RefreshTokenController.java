package com.sparta.realtomatoapp.security.refreshToken.controller;

import com.sparta.realtomatoapp.common.dto.BaseResponseDto;
import com.sparta.realtomatoapp.security.config.JwtProvider;
import com.sparta.realtomatoapp.security.refreshToken.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    @PostMapping("/refresh-token")
    public ResponseEntity<BaseResponseDto> reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
        // 리프레시 토큰이 없을 시
        if(refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponseDto.baseResponseBuilder().message("리프레시 토큰이 존재하지 않습니다.").build());
        }

        // 리프레시 토큰 유효성 검사 실패 시
        if(!jwtProvider.verifyRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponseDto.baseResponseBuilder().message("리프레시 토큰이 유효하지 않습니다.").build());
        }

        String newAccessToken = refreshTokenService.reissueAccessToken(refreshToken);


        // 헤더에 새로운 엑세스 토큰 발급
        jwtProvider.addAccessTokenToHeader(response, newAccessToken);

        return ResponseEntity.ok(BaseResponseDto.baseResponseBuilder().message("엑세스 토큰 재발급 성공").build());
    }
}
