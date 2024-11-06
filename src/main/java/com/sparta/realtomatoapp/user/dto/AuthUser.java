package com.sparta.realtomatoapp.user.dto;

import com.sparta.realtomatoapp.user.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthUser {
    private final Long userId;
    private final String email;
    private final UserRole role;

    public AuthUser(Long userId, String email, UserRole role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }
}