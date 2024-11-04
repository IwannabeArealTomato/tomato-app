package com.sparta.realtomatoapp.auth.dto;

import lombok.Getter;

@Getter
public class UserRegistrationRequestDTO {
    private String userName;
    private String email;
    private String password;
    private String userRole;
    private String address;
}
