package com.sparta.realtomatoapp.domain.user.service;

import com.sparta.realtomatoapp.domain.user.dto.SignupRequestDTO;
import com.sparta.realtomatoapp.domain.user.entity.User;
import com.sparta.realtomatoapp.domain.user.repository.UserRepository;
import com.sparta.realtomatoapp.domain.user.common.UserRoleEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String registerUser(SignupRequestDTO signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // UserRoleEnum을 직접 사용
        UserRoleEnum role = signupRequest.getUserRole();

        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .role(role)
                .userName(signupRequest.getUserName())
                .address(signupRequest.getAddress())
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }
}
