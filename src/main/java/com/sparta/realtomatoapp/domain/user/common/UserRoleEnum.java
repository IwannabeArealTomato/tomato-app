package com.sparta.realtomatoapp.domain.user.common;

public enum UserRoleEnum {
    USER("ROLE_USER"),
    STORE_OWNER("ROLE_STORE_OWNER"),
    ADMIN("ROLE_ADMIN"); // 관리자 역할 추가

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }
}
