package com.sparta.realtomatoapp.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    GUEST("ROLE_GUEST");

    private String value;

}