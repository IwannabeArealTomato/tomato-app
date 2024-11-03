package com.sparta.realtomatoapp;

import com.sparta.realtomatoapp.domain.user.dto.LoginRequestDTO;
import com.sparta.realtomatoapp.domain.user.dto.SignupRequestDTO;
import com.sparta.realtomatoapp.domain.user.entity.User;
import com.sparta.realtomatoapp.domain.user.repository.UserRepository;
import com.sparta.realtomatoapp.domain.user.service.SignupService;
import com.sparta.realtomatoapp.jwt.dto.TokenResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional // 테스트가 끝난 후 데이터베이스 롤백
class SignupServiceTest {

    @Autowired
    private SignupService signupService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private SignupRequestDTO signupRequestDTO;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setUp() {
        signupRequestDTO = new SignupRequestDTO();
        signupRequestDTO.setEmail("test@example.com");
        signupRequestDTO.setPassword("password123");
        signupRequestDTO.setUserName("Test User");
        signupRequestDTO.setAddress("Test Address");
        signupRequestDTO.setAdmin(false);

        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail(signupRequestDTO.getEmail());
        loginRequestDTO.setPassword(signupRequestDTO.getPassword());
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signupSuccessTest() {
        // when
        String result = signupService.signup(signupRequestDTO);

        // then
        assertThat(result).isEqualTo("User registered successfully");

        // DB에 유저가 저장되었는지 확인
        User user = userRepository.findByEmail(signupRequestDTO.getEmail()).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.getUserName()).isEqualTo(signupRequestDTO.getUserName());
    }

    @Test
    @DisplayName("회원가입 중복 이메일 테스트")
    void signupDuplicateEmailTest() {
        // Given - 먼저 유저를 하나 저장
        signupService.signup(signupRequestDTO);

        // When & Then - 동일한 이메일로 회원가입 시도
        assertThrows(IllegalArgumentException.class, () -> signupService.signup(signupRequestDTO));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccessTest() {
        // Given - 유저를 먼저 저장
        signupService.signup(signupRequestDTO);

        // When
        TokenResponseDTO tokenResponseDTO = signupService.login(loginRequestDTO);

        // Then
        assertThat(tokenResponseDTO.getAccessToken()).isNotNull();
        assertThat(tokenResponseDTO.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 잘못된 비밀번호")
    void loginFailTest() {
        // Given - 유저를 먼저 저장
        signupService.signup(signupRequestDTO);

        // When & Then - 잘못된 비밀번호로 로그인 시도
        loginRequestDTO.setPassword("wrongPassword");
        assertThrows(IllegalArgumentException.class, () -> signupService.login(loginRequestDTO));
    }
}
