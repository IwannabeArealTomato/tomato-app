package com.sparta.realtomatoapp.user.dto;

import com.sparta.realtomatoapp.user.entity.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class AuthUser {
    private Long userId;
    private String email;
    private UserRole role;
}