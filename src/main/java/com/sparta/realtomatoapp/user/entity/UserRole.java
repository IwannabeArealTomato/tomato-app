package com.sparta.realtomatoapp.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {

    USER("ROLE_USER"),
    STOREOWNER("ROLE_STOREOWNER"),
    ADMIN("ROLE_ADMIN");

    private String value;

}