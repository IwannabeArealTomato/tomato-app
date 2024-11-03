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
    private UserRoleEnum userRole;

    // 문자열을 UserRoleEnum으로 설정하는 메서드 추가
    public void setUserRole(String role) {
        this.userRole = UserRoleEnum.valueOf(role.toUpperCase());
    }
}
