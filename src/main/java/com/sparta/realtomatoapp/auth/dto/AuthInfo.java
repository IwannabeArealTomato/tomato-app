package com.sparta.realtomatoapp.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthInfo {
    private String email;
    private String role;
}
