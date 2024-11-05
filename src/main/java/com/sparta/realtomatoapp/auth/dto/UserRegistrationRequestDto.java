package com.sparta.realtomatoapp.auth.dto;

import com.sparta.realtomatoapp.user.entity.UserRole;
import com.sparta.realtomatoapp.user.entity.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequestDto {
    private String email;
    private String userName;
    private String password;
    private String address;
    private UserRole role;
    private UserStatus status;
}
