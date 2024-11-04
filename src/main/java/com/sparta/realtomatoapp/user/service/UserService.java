package com.sparta.realtomatoapp.user.service;

import com.sparta.realtomatoapp.auth.dto.UserRegistrationRequestDTO;
import com.sparta.realtomatoapp.auth.dto.UserResponseDTO;
import com.sparta.realtomatoapp.user.entity.UserStatus;
import com.sparta.realtomatoapp.user.repository.UserRepository;
import com.sparta.realtomatoapp.security.util.PasswordEncoder;
import com.sparta.realtomatoapp.user.entity.User;
import com.sparta.realtomatoapp.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoderUtil;

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserResponseDTO registerUser(UserRegistrationRequestDTO request) {
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

    public UserResponseDTO convertToDto(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .build();
    }
}
