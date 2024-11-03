// UserRoleEnum.java
package com.sparta.realtomatoapp.domain.user.common;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    USER("ROLE_USER"),
    STORE_OWNER("ROLE_STORE_OWNER"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    // String 역할을 UserRoleEnum으로 변환하는 메서드 추가
    public static UserRoleEnum fromString(String role) {
        return switch (role.toUpperCase()) {
            case "ROLE_STORE_OWNER" -> STORE_OWNER;
            case "ROLE_ADMIN" -> ADMIN;
            default -> USER;
        };
    }
}
