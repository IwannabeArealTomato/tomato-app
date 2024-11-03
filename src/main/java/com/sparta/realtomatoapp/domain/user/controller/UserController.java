package com.sparta.realtomatoapp.domain.user.controller;

import com.sparta.realtomatoapp.domain.user.dto.LoginRequestDTO;
import com.sparta.realtomatoapp.domain.user.dto.SignupRequestDTO;
import com.sparta.realtomatoapp.jwt.dto.TokenResponseDTO;
import com.sparta.realtomatoapp.domain.user.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class UserController {

    private final LoginService loginService;

    public UserController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        TokenResponseDTO tokenResponse = loginService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDTO signupRequest) {
        String response = loginService.signup(signupRequest);
        return ResponseEntity.ok(response);
    }
}
