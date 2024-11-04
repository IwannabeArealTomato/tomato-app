package com.sparta.realtomatoapp.domain.user.service;

import com.sparta.realtomatoapp.auth.config.JwtProvider;
import com.sparta.realtomatoapp.auth.dto.AuthInfo;
import com.sparta.realtomatoapp.domain.user.entity.User;
import com.sparta.realtomatoapp.domain.user.repository.UserRepositoy;
import com.sparta.realtomatoapp.security.util.PasswordEncoderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepositoy userRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;
    private final JwtProvider jwtProvider; // JWT 토큰 생성을 위해 필요

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String loginUser(String email, String password) {
        User user = findUserByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("이메일이 존재하지 않습니다."));

        if (!passwordEncoderUtil.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        AuthInfo authInfo = AuthInfo.builder()
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();

        return jwtProvider.createJwtToken(authInfo);
    }
}
