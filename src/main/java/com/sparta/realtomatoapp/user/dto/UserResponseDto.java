package com.sparta.realtomatoapp.user.dto;

import com.sparta.realtomatoapp.user.entity.User;
import com.sparta.realtomatoapp.user.entity.UserRole;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponseDto {
    private Long userId;
    private String email;
    private String username;
    private UserRole role;
}
