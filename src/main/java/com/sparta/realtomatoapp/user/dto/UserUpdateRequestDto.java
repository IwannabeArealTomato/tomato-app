package com.sparta.realtomatoapp.user.dto;

import lombok.Getter;

@Getter
public class UserUpdateRequestDto {
    private String userName;
    private String address;
    private String pastPassword;
    private String newPassword;
}
