package com.sparta.realtomatoapp.domain.user.service;

import com.sparta.realtomatoapp.domain.user.dto.LoginRequestDTO;
import com.sparta.realtomatoapp.domain.user.dto.SignupRequestDTO;
import com.sparta.realtomatoapp.domain.user.entity.User;
import com.sparta.realtomatoapp.domain.user.repository.UserRepository;
import com.sparta.realtomatoapp.domain.user.common.UserRoleEnum;
import com.sparta.realtomatoapp.jwt.dto.TokenResponseDTO;
import com.sparta.realtomatoapp.jwt.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // 생성자를 통해 모든 필드를 초기화
    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public TokenResponseDTO login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String accessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail(), user.getRole());

        return new TokenResponseDTO(accessToken, refreshToken);
    }

    @Transactional
    public String signup(SignupRequestDTO signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

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
