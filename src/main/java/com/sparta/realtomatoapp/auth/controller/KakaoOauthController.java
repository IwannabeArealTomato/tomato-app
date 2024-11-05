package com.sparta.realtomatoapp.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.realtomatoapp.auth.service.KakaoOauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/oauth2.0/kakao")
@RequestMapping("/api/auth/kakao")
public class KakaoOauthController {

    private final KakaoOauthService kakaoOauthService;


    @GetMapping("/login")
    public String redirectKakaoLogin(
            @Value("${kakao.auth.url}") String authUrl,
            @Value("${kakao.client.id}") String clientId,
            @Value("${kakao.redirect.uri}") String redirectUri
    ) {
        return authUrl +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code";
    }

    @GetMapping("/callback")
    public String callbackKakaoLogin(@RequestParam String code) throws JsonProcessingException {
        String accessToken = kakaoOauthService.kakaoLogin(code);

        return accessToken;
    }
}
