package com.sparta.realtomatoapp.domain.user.controller;

import com.sparta.realtomatoapp.domain.user.dto.LoginRequestDTO;
import com.sparta.realtomatoapp.domain.user.dto.SignupRequestDTO;
import com.sparta.realtomatoapp.jwt.dto.TokenResponseDTO;
import com.sparta.realtomatoapp.domain.user.service.AuthService;
import com.sparta.realtomatoapp.domain.user.service.SignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final AuthService authService;
    private final SignupService signupService;

    public UserController(AuthService authService, SignupService signupService) {
        this.authService = authService;
        this.signupService = signupService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        TokenResponseDTO tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@RequestHeader("RefreshToken") String oldRefreshToken) {
        TokenResponseDTO tokenResponse = authService.refreshToken(oldRefreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDTO signupRequest) {
        String response = signupService.registerUser(signupRequest);
        return ResponseEntity.ok(response);
    }
}
