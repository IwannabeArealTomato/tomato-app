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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto> login(@RequestBody LoginRequestDto request) {
        log.info("AuthController.login");

        try {
            String jwtToken = userService.loginUser(request);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwtToken);

            return ResponseEntity.ok()
                    .headers(headers)
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

    @PostMapping("/signup")
    public ResponseEntity<DataResponseDto> signup(@RequestBody UserRegistrationRequestDto request) {
        log.info("AuthController.signup");

        // 사용자 등록
        UserResponseDto userResponseDto = userService.registerUser(request);

        return ResponseEntity.ok(
                DataResponseDto.<UserResponseDto>dataResponseBuilder()
                .message("회원 가입 성공")
                .data(List.of(userResponseDto))
                .build()
        );
    }
}
