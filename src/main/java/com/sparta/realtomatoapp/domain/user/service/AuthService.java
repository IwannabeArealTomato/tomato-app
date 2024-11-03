package com.sparta.realtomatoapp.domain.user.service;

import com.sparta.realtomatoapp.domain.user.dto.LoginRequestDTO;
import com.sparta.realtomatoapp.domain.user.entity.User;
import com.sparta.realtomatoapp.domain.user.repository.UserRepository;
import com.sparta.realtomatoapp.jwt.dto.TokenResponseDTO;
import com.sparta.realtomatoapp.jwt.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Transactional
    public TokenResponseDTO login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail(), user.getRole());

        return new TokenResponseDTO(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponseDTO refreshToken(String oldRefreshToken) {
        String email = jwtUtil.extractUsername(oldRefreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String newAccessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole());
        String newRefreshToken = jwtUtil.createRefreshToken(user.getEmail(), user.getRole());

        return new TokenResponseDTO(newAccessToken, newRefreshToken);
    }
}
