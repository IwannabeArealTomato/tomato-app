package com.sparta.realtomatoapp.user.controller;

import com.sparta.realtomatoapp.common.dto.BaseResponseDto;
import com.sparta.realtomatoapp.common.entity.LoginUser;
import com.sparta.realtomatoapp.user.dto.AuthUser;
import com.sparta.realtomatoapp.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    /**
     * LoginUserResolver 사용 예시
     * @param authUser request header에 포함된 jwt 토큰
     * @return ResponseEntity
     */
    @GetMapping("/user")
    public ResponseEntity<BaseResponseDto> getUserInfo(@LoginUser AuthUser authUser) {
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
}
