package com.sparta.realtomatoapp.auth.controller;

import com.sparta.realtomatoapp.security.config.JwtProvider;
import com.sparta.realtomatoapp.auth.dto.AuthInfo;
import com.sparta.realtomatoapp.auth.dto.LoginDto;
import com.sparta.realtomatoapp.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> signup(@RequestBody UserRegistrationRequest request) {
        log.info("AuthController.signup");

        // 사용자 등록
        userService.registerUser(
                request.getEmail(),
                request.getUserName(),
                request.getPassword(),
                request.getAddress()
        );

        return ResponseEntity.ok("사용자 등록이 완료되었습니다.");
    }
}
