package com.sparta.realtomatoapp.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.realtomatoapp.auth.service.GoogleOauthService;
import com.sparta.realtomatoapp.auth.service.KakaoOauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/google")
public class GoogleOauthController {

    private final GoogleOauthService googleOauthService;


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
    public String callbackGoogleLogin(@RequestParam String code) throws JsonProcessingException {
        String accessToken = googleOauthService.googleLogin(code);

        return accessToken;
    }
}
