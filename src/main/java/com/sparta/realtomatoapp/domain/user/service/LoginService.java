package com.sparta.realtomatoapp.domain.user.service;

import com.sparta.realtomatoapp.domain.user.common.UserRoleEnum;
import com.sparta.realtomatoapp.domain.user.dto.LoginRequestDTO;
import com.sparta.realtomatoapp.jwt.dto.TokenResponseDTO;
import com.sparta.realtomatoapp.domain.user.entity.User;
import com.sparta.realtomatoapp.domain.user.repository.UserRepository;
import com.sparta.realtomatoapp.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public TokenResponseDTO login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticateUser(loginRequest);
        User user = findUserByEmail(loginRequest.getEmail());

        String accessToken = jwtUtil.createToken(user.getEmail(), user.getRole(), false);
        String refreshToken = jwtUtil.createToken(user.getEmail(), user.getRole(), true);

        return new TokenResponseDTO(accessToken, refreshToken);
    }

    public TokenResponseDTO refreshToken(String oldRefreshToken) {
        String email = jwtUtil.extractUsernameFromToken(oldRefreshToken);
        String newRefreshToken = jwtUtil.rotateRefreshToken(oldRefreshToken);
        String accessToken = jwtUtil.createToken(email, UserRoleEnum.USER, false);

        return new TokenResponseDTO(accessToken, newRefreshToken);
    }

    private Authentication authenticateUser(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
