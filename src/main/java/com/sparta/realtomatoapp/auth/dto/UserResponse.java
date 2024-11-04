package com.sparta.realtomatoapp.auth.dto;

import com.sparta.realtomatoapp.user.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private Long userId;
    private String userName;
    private String email;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
