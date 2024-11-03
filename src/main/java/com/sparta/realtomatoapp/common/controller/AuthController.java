package com.sparta.realtomatoapp.common.controller;

import com.sparta.realtomatoapp.common.dto.LoginRequestDTO;
import com.sparta.realtomatoapp.common.dto.SignupRequestDTO;
import com.sparta.realtomatoapp.common.dto.TokenResponseDTO;
import com.sparta.realtomatoapp.domain.user.common.UserRoleEnum;
import com.sparta.realtomatoapp.domain.user.entity.User;
import com.sparta.realtomatoapp.domain.user.repository.UserRepository;
import com.sparta.realtomatoapp.jwt.util.JwtUtil;
import com.sparta.realtomatoapp.common.service.SignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final SignupService signupService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository, SignupService signupService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.signupService = signupService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticateUser(loginRequest);
        User user = findUserByEmail(loginRequest.getEmail());

        String accessToken = jwtUtil.createToken(user.getEmail(), user.getRole(), false);
        String refreshToken = jwtUtil.createToken(user.getEmail(), user.getRole(), true);

        return ResponseEntity.ok(new TokenResponseDTO(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@RequestHeader("RefreshToken") String oldRefreshToken) {
        String email = jwtUtil.extractUsernameFromToken(oldRefreshToken);
        String newRefreshToken = jwtUtil.rotateRefreshToken(oldRefreshToken);
        String accessToken = jwtUtil.createToken(email, UserRoleEnum.USER, false);
        return ResponseEntity.ok(new TokenResponseDTO(accessToken, newRefreshToken));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDTO signupRequest) {
        String response = signupService.registerUser(signupRequest);
        return ResponseEntity.ok(response);
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
