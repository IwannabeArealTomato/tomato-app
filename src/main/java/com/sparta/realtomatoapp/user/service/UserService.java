package com.sparta.realtomatoapp.user.service;

import com.sparta.realtomatoapp.auth.dto.LoginRequestDto;
import com.sparta.realtomatoapp.auth.dto.UserRegistrationRequestDto;
import com.sparta.realtomatoapp.auth.dto.UserResponseDto;
import com.sparta.realtomatoapp.common.exception.CustomException;
import com.sparta.realtomatoapp.common.exception.eunm.ErrorCode;
import com.sparta.realtomatoapp.security.config.JwtProvider;
import com.sparta.realtomatoapp.security.refreshToken.service.RefreshTokenService;
import com.sparta.realtomatoapp.user.entity.User;
import com.sparta.realtomatoapp.user.entity.UserRole;
import com.sparta.realtomatoapp.user.entity.UserStatus;
import com.sparta.realtomatoapp.user.repository.UserRepository;
import com.sparta.realtomatoapp.security.util.PasswordEncoderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Map<String, String> loginUserWithTokens(LoginRequestDto request) {
        log.info("Fetching user by email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));

        if (!passwordEncoderUtil.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // Access Token 및 Refresh Token 생성
        String accessToken = jwtProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());

        // Refresh Token을 데이터베이스에 저장
        refreshTokenService.createAndStoreRefreshToken(user.getEmail(), refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    public UserResponseDto registerUser(UserRegistrationRequestDto request) {
        if (findUserByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoderUtil.encode(request.getPassword());
        UserRole userRole = request.getRole();

        User user = User.builder()
                .email(request.getEmail())
                .userName(request.getUserName())
                .password(encodedPassword)
                .role(userRole)
                .status(request.getStatus() != null ? request.getStatus() : UserStatus.ACTIVE)
                .address(request.getAddress())
                .build();

        userRepository.save(user);
        return convertToDto(user);
    }

    public String refreshAccessToken(String refreshToken) {
        if (!refreshTokenService.isTokenValid(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }
        String username = jwtProvider.getUserFromRefreshToken(refreshToken);
        return jwtProvider.generateAccessToken(username);
    }

    public boolean verifyRefreshToken(String refreshToken) {
        return refreshTokenService.isTokenValid(refreshToken);
    }

    public void invalidateRefreshToken(String refreshToken) {
        refreshTokenService.invalidateRefreshToken(refreshToken);
    }

    private UserResponseDto convertToDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .build();
    }

    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND));

        return convertToDto(user);
    }

    public List<UserResponseDto> getAllUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageRequest);

        return userPage.stream()
                .map(user -> UserResponseDto.builder()
                        .userId(user.getUserId())
                        .userName(user.getUserName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .createdAt(user.getCreatedAt())
                        .modifiedAt(user.getModifiedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
