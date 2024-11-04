package com.sparta.realtomatoapp.auth.controller;

import com.sparta.realtomatoapp.auth.config.JwtProvider;
import com.sparta.realtomatoapp.auth.dto.AuthInfo;
import com.sparta.realtomatoapp.auth.dto.LoginDto;
import com.sparta.realtomatoapp.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        log.info("AuthController.login");
        AuthInfo authInfo = AuthInfo.builder()
                .email(loginDto.getEmail())
                .role(String.valueOf(UserRole.GUEST))
                .build();

        String jwtToken = jwtProvider.createJwtToken(authInfo);
        return jwtToken;
    }

}
