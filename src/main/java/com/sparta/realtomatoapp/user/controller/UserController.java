package com.sparta.realtomatoapp.user.controller;

import com.sparta.realtomatoapp.auth.dto.UserResponseDto;
import com.sparta.realtomatoapp.common.dto.BaseResponseDto;
import com.sparta.realtomatoapp.common.dto.DataResponseDto;
import com.sparta.realtomatoapp.common.entity.LoginUser;
import com.sparta.realtomatoapp.user.dto.AuthUser;
import com.sparta.realtomatoapp.user.entity.UserRole;
import com.sparta.realtomatoapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    /**
     * LoginUserResolver 사용 예시
     * @param authUser request header에 포함된 jwt 토큰
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<BaseResponseDto> exampleResolver(@LoginUser AuthUser authUser) {
        if (authUser != null) {
            String email = authUser.getEmail();
            UserRole role = authUser.getRole();
            Long userId = authUser.getUserId();

            return ResponseEntity.ok()
                    .body(
                            BaseResponseDto.baseResponseBuilder()
                                    .message("userId : " + userId + "| email : " + email + "| role : " + role)
                                    .build()
                    );
        }
        return ResponseEntity.badRequest().build();
    }

    // 회원 단건 조회
    @GetMapping("/{userId}")
    public ResponseEntity<DataResponseDto> getUserInfo(@PathVariable Long userId) {

        UserResponseDto userResponse = userService.getUserById(userId);
        return ResponseEntity.ok()
                .body(
                        DataResponseDto.dataResponseBuilder()
                                .message("조회 성공")
                                .data(Collections.singletonList(userResponse))
                                .build()
                );
    }

}
