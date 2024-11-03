package com.sparta.realtomatoapp.security;

import com.sparta.realtomatoapp.domain.user.common.UserRoleEnum;
import com.sparta.realtomatoapp.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

//UserDetailsImpl 클래스는 Spring Security에서 사용자의 인증 정보를 나타내는 클래스입니다.
//Spring Security의 UserDetails 인터페이스를 구현하여 사용자 정보와 인증 상태를 관리합니다.

public class UserDetailsImpl implements UserDetails {

    // 인증된 사용자 정보를 저장하는 User 객체
    private final User user;

    // 사용자의 역할 정보
    private final UserRoleEnum role;


    //User 엔터티 객체를 받아서 user와 role 필드를 초기화합니다.

    public UserDetailsImpl(User user) {
        this.user = user;
        this.role = user.getRole(); // 사용자의 역할 정보를 초기화
    }

    public UserRoleEnum getRole() {
        return role;
    }


    //사용자의 이름(이메일)을 반환하는 메서드
    //UserDetails 인터페이스의 getUsername 메서드를 구현하여 사용자의 이메일을 반환합니다.

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    //사용자의 권한 목록을 반환하는 메서드

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    //사용자의 비밀번호를 반환하는 메서드

    @Override
    public String getPassword() {
        return user.getPassword();
    }


     //계정이 만료되지 않았음을 나타내는 메서드

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠기지 않았음을 나타내는 메서드

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //자격 증명이 만료되지 않았음을 나타내는 메서드

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화되었음을 나타내는 메서드

    @Override
    public boolean isEnabled() {
        return true;
    }
}
