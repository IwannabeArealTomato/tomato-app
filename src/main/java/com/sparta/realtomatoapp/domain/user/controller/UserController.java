package com.sparta.realtomatoapp.domain.user.controller;

import com.sparta.realtomatoapp.domain.user.dto.LoginRequestDTO;
import com.sparta.realtomatoapp.domain.user.dto.SignupRequestDTO;
import com.sparta.realtomatoapp.jwt.dto.TokenResponseDTO;
import com.sparta.realtomatoapp.domain.user.service.LoginService;
import com.sparta.realtomatoapp.domain.user.service.SignupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final LoginService loginService;
    private final SignupService signupService;

    public UserController(LoginService loginService, SignupService signupService) {
        this.loginService = loginService;
        this.signupService = signupService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        TokenResponseDTO tokenResponse = loginService.login(loginRequest);
        if (tokenResponse != null) {
            return ResponseEntity.ok(tokenResponse);  // 로그인 성공 시 토큰 정보 반환
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 이메일 또는 비밀번호를 확인하세요.");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("RefreshToken") String oldRefreshToken) {
        TokenResponseDTO tokenResponse = loginService.refreshToken(oldRefreshToken);
        if (tokenResponse != null) {
            return ResponseEntity.ok(tokenResponse);  // 토큰 갱신 성공 시 새로운 토큰 반환
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 갱신 실패: 유효하지 않은 리프레시 토큰입니다.");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDTO signupRequest) {
        try {
            String response = signupService.registerUser(signupRequest);
            return ResponseEntity.ok(response);  // 회원가입 성공 시 성공 메시지 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패: " + e.getMessage());
        }
    }
}
