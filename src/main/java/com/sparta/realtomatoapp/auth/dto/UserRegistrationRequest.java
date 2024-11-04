package com.sparta.realtomatoapp.auth.dto;

import com.sparta.realtomatoapp.user.entity.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserRegistrationRequest {
    private String userName;
    private String email;
    private String password;
    private String userRole;
    private String address;
}
