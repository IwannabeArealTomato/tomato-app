package com.sparta.realtomatoapp.domain.user.dto;

import com.sparta.realtomatoapp.domain.user.common.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequestDTO {
    private String email;
    private String password;
    private String userName;
    private String address;
    private UserRoleEnum role; // 역할을 UserRoleEnum으로 받음

    public boolean isAdmin() {
        return role == UserRoleEnum.ADMIN;
    }
}
