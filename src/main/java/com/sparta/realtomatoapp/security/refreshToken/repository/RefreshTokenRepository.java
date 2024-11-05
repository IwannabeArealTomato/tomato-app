package com.sparta.realtomatoapp.security.refreshToken.repository;

import com.sparta.realtomatoapp.security.refreshToken.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserEmail(String userEmail); // 이메일로 조회하는 메서드 추가
    void deleteByToken(String token);
}
