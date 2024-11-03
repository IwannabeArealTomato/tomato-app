package com.sparta.realtomatoapp.common.service;

import com.sparta.realtomatoapp.domain.user.common.UserRoleEnum;
import com.sparta.realtomatoapp.domain.user.entity.User;
import com.sparta.realtomatoapp.domain.user.repository.UserRepository;
import com.sparta.realtomatoapp.common.dto.SignupRequestDTO;
import com.sparta.realtomatoapp.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public SignupService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public String registerUser(SignupRequestDTO requestDTO) {
        String email = requestDTO.getEmail();
        String password = passwordEncoder.encode(requestDTO.getPassword());

        // 이메일 중복 확인
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // ADMIN 권한 설정 여부 확인
        UserRoleEnum role = requestDTO.isAdmin() ? UserRoleEnum.ADMIN : UserRoleEnum.USER;

        // User 엔터티 생성
        User user = User.builder()
                .email(email)
                .password(password)
                .role(role)
                .userName(requestDTO.getUserName())
                .address(requestDTO.getAddress())
                .build();

        userRepository.save(user);

        return "User registered successfully";
    }
}
