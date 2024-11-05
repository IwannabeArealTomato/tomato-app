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
import java.util.HashMap;
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
            headers.add("RefreshToken", refreshToken);

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
    public ResponseEntity<Map<String, Object>> refreshAccessToken(@RequestHeader("Refresh-Token") String refreshToken) {
        log.info("AuthController.refreshAccessToken");

        if (!userService.verifyRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "유효하지 않은 또는 만료된 Refresh Token입니다."));
        }

        String newAccessToken = userService.refreshAccessToken(refreshToken);
        String newRefreshToken = userService.generateNewRefreshTokenIfNeeded(refreshToken);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Access Token 재발급 성공");
        responseBody.put("accessToken", newAccessToken);

        // 만약 리프레쉬 토큰이 갱신된 경우, 바디에 포함
        if (newRefreshToken != null) {
            responseBody.put("refreshToken", newRefreshToken);
        }

        return ResponseEntity.ok(responseBody);
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
