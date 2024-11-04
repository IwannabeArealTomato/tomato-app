package com.sparta.realtomatoapp.auth.controller;

import com.sparta.realtomatoapp.auth.dto.UserRegistrationRequest;
import com.sparta.realtomatoapp.auth.dto.UserResponseDto;
import com.sparta.realtomatoapp.common.dto.ApiResponse;
import com.sparta.realtomatoapp.security.config.JwtProvider;
import com.sparta.realtomatoapp.auth.dto.AuthInfo;
import com.sparta.realtomatoapp.auth.dto.LoginDto;
import com.sparta.realtomatoapp.user.entity.User;
import com.sparta.realtomatoapp.user.entity.UserRole;
import com.sparta.realtomatoapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtProvider jwtProvider;
    private final UserService userService;

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

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody UserRegistrationRequest request) {
        log.info("AuthController.signup");

        // 사용자 등록
        User user = userService.registerUser(request);

        List<UserResponseDto> userResponseDto = List.of(userService.convertToDto(user));

        ApiResponse response = ApiResponse.builder()
                .message("회원 가입 성공")
                .data(userResponseDto)
                .build();

        return ResponseEntity.ok(response);
    }
}
