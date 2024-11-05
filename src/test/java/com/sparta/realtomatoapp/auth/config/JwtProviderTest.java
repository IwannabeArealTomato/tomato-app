package com.sparta.realtomatoapp.auth.config;

import com.sparta.realtomatoapp.security.config.JwtProvider;
import com.sparta.realtomatoapp.user.dto.AuthUser;
import com.sparta.realtomatoapp.user.entity.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void test_jwtProvider() {
        //given
        String mail = "test@test.com";
        UserRole role = UserRole.GUEST;

        AuthUser authUser = AuthUser.builder()
                .email(mail)
                .role(role)
                .build();

        //when
        String jwtToken = jwtProvider.createJwtToken(authUser);

        //then
        AuthUser currentRequestAuthInfo = jwtProvider.getCurrentRequestAuthInfo(jwtToken);

        Assertions.assertThat(jwtToken).isNotNull();
        Assertions.assertThat(jwtProvider.verifyAccessToken(jwtToken)).isTrue();
//        Assertions.assertThat(currentRequestAuthInfo.getEmail()).isEqualTo(mail);
//        Assertions.assertThat()
//        Assertions.assertThat(currentRequestAuthInfo.getRole()).isEqualTo(role);

    }
}