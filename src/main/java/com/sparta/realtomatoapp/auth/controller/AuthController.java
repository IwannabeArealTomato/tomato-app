package com.sparta.realtomatoapp.auth.controller;

import com.sparta.realtomatoapp.auth.dto.LoginDto;
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

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        log.info("AuthController.login");
        return "SUCCESS";
    }

}
