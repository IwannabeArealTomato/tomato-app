package com.sparta.realtomatoapp.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.realtomatoapp.auth.dto.OauthLoginResponseDto;
import com.sparta.realtomatoapp.auth.service.GoogleOauthService;
import com.sparta.realtomatoapp.common.dto.BaseResponseDto;
import com.sparta.realtomatoapp.security.config.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/google")
public class GoogleOauthController {

    private final GoogleOauthService googleOauthService;
    private final JwtProvider jwtProvider;


    @GetMapping("/login")
    public String redirectGoogleLogin(
            @Value("${google.auth.url}") String authUrl,
            @Value("${google.client.id}") String clientId,
            @Value("${google.redirect.uri}") String redirectUri
    ) {
        return authUrl +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=openid profile email";
    }

    @GetMapping("/callback")
    public ResponseEntity<BaseResponseDto> callbackGoogleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        try {
            OauthLoginResponseDto tokens = googleOauthService.googleLogin(code);

            // 헤더와 쿠키에 엑세스토큰 과 리프레시 토큰을 저장
            jwtProvider.addAccessTokenToHeader(response, tokens.getAccessToken());
            jwtProvider.addRefreshTokenToCookie(response, tokens.getRefreshToken());

            return ResponseEntity.ok()
                    .body(
                            BaseResponseDto.baseResponseBuilder()
                                    .message("로그인 성공")
                                    .build()
                    );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(
                            BaseResponseDto.baseResponseBuilder()
                                    .message("로그인 실패" + e.getMessage())
                                    .build()
                    );
        }
    }
}
