package com.sparta.realtomatoapp.domain.user.service;

import com.sparta.realtomatoapp.domain.user.entity.User;
import com.sparta.realtomatoapp.domain.user.entity.UserRole;
import com.sparta.realtomatoapp.domain.user.repository.UserRepositoy;
import com.sparta.realtomatoapp.security.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepositoy userRepository;
    private final PasswordEncoder passwordEncoderUtil;

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerUser(String email, String userName, String password, String address) {
        // 이메일 중복 체크
        if (findUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoderUtil.encode(password);

        // 기본 사용자 역할 설정
        User user = User.builder()
                .email(email)
                .userName(userName)
                .password(encodedPassword)
                .role(UserRole.USER) // 기본 역할 설정
                .address(address)
                .build();

        return userRepository.save(user);
    }
}
