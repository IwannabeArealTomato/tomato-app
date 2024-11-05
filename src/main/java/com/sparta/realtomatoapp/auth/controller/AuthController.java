package com.sparta.realtomatoapp.auth.controller;

import com.sparta.realtomatoapp.auth.dto.UserRegistrationRequestDto;
import com.sparta.realtomatoapp.auth.dto.UserResponseDto;
import com.sparta.realtomatoapp.common.dto.BaseResponseDto;
import com.sparta.realtomatoapp.common.dto.DataResponseDto;
import com.sparta.realtomatoapp.auth.dto.LoginRequestDto;
import com.sparta.realtomatoapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto> login(@RequestBody LoginRequestDto request) {
        log.info("Attempting login for email: {}", request.getEmail());

        try {
            Map<String, String> tokens = userService.loginUserWithTokens(request);
            String accessToken = tokens.get("accessToken");
            String refreshToken = tokens.get("refreshToken");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Refresh-Token", refreshToken);

            log.info("Login successful for email: {}", request.getEmail());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(BaseResponseDto.baseResponseBuilder()
                            .message("로그인 성공")
                            .build());
        } catch (IllegalArgumentException e) {
            log.error("Login failed for email: {} - Reason: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(BaseResponseDto.baseResponseBuilder()
                            .message("로그인 실패: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<DataResponseDto> signup(@RequestBody UserRegistrationRequestDto request) {
        log.info("AuthController.signup");

        UserResponseDto userResponseDto = userService.registerUser(request);

        return ResponseEntity.ok(DataResponseDto.<UserResponseDto>dataResponseBuilder()
                .message("회원 가입 성공")
                .data(Collections.singletonList(userResponseDto))
                .build());
    }
    @PostMapping("/refresh")
    public ResponseEntity<BaseResponseDto> refreshAccessToken(@RequestHeader("Refresh-Token") String refreshToken) {
        log.info("AuthController.refreshAccessToken");

        if (!userService.verifyRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN) // 변경
                    .body(BaseResponseDto.baseResponseBuilder()
                            .message("유효하지 않은 또는 만료된 Refresh Token입니다.")
                            .build());
        }

        String newAccessToken = userService.refreshAccessToken(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + newAccessToken);

        return ResponseEntity.ok()
                .headers(headers)
                .body(BaseResponseDto.baseResponseBuilder()
                        .message("Access Token 재발급 성공")
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponseDto> logout(@RequestHeader("Refresh-Token") String refreshToken) {
        log.info("AuthController.logout");

        userService.invalidateRefreshToken(refreshToken);

        return ResponseEntity.ok(BaseResponseDto.baseResponseBuilder()
                .message("로그아웃 성공")
                .build());
    }
}
