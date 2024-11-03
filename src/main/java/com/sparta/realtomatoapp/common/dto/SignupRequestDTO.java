package com.sparta.realtomatoapp.common.dto;

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
    private boolean isAdmin; // 관리자 여부

    public boolean isAdmin() {
        return isAdmin;
    }
}
