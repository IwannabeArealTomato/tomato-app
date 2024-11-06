package com.sparta.realtomatoapp.security.refreshToken.repository;

import com.sparta.realtomatoapp.security.refreshToken.entity.RefreshToken;
import com.sparta.realtomatoapp.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findByTokenValue(String tokenValue);

    void deleteByTokenValue(String tokenValue);
}
