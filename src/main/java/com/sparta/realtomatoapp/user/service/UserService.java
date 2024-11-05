package com.sparta.realtomatoapp.user.service;

import com.sparta.realtomatoapp.auth.dto.LoginRequestDto;
import com.sparta.realtomatoapp.auth.dto.UserRegistrationRequestDto;
import com.sparta.realtomatoapp.auth.dto.UserResponseDto;
import com.sparta.realtomatoapp.security.config.JwtProvider;
import com.sparta.realtomatoapp.security.exception.CustomException;
import com.sparta.realtomatoapp.security.exception.eunm.ErrorCode;
import com.sparta.realtomatoapp.security.util.PasswordEncoderUtil;
import com.sparta.realtomatoapp.user.dto.AuthUser;
import com.sparta.realtomatoapp.user.dto.UserUpdateRequestDto;
import com.sparta.realtomatoapp.user.entity.UserStatus;
import com.sparta.realtomatoapp.user.repository.UserRepository;
import com.sparta.realtomatoapp.user.entity.User;
import com.sparta.realtomatoapp.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;
    private final JwtProvider jwtProvider; // JWT 토큰 생성을 위해 필요

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String loginUser(LoginRequestDto request) {
        User user = findUserByEmail(request.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("이메일이 존재하지 않습니다."));

        if (!passwordEncoderUtil.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        AuthUser authUser = AuthUser.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return jwtProvider.createJwtToken(authUser);
    }

    public UserResponseDto registerUser(UserRegistrationRequestDto request) {
        // 이메일 중복 체크
        if (findUserByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoderUtil.encode(request.getPassword());

        UserRole userRole = UserRole.valueOf(request.getUserRole());

        // 기본 사용자 역할 설정
        User user = User.builder()
                .email(request.getEmail())
                .userName(request.getUserName())
                .password(encodedPassword)
                .role(userRole)
                .status(UserStatus.ACTIVE) // 활성화 상태
                .address(request.getAddress())
                .build();

        userRepository.save(user);

        return convertToDto(user);
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

    public User updateUser(Long userId, UserUpdateRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoderUtil.matches(request.getPastPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        if (request.getUserName() != null) {
            user.setUserName(request.getUserName());
        }
        if (request.getNewPassword() != null) {
            user.setPassword(request.getNewPassword());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }

        return userRepository.save(user);
    }

    public void deactivateUser(Long userId, String password) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoderUtil.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        userRepository.deleteById(userId);
    }

    public UserResponseDto convertToDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .build();
    }
}
