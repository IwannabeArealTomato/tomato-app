package com.sparta.realtomatoapp.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {
    private String userName;
    private String email;
    private String password;
    private String address;
}
