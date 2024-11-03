package com.sparta.realtomatoapp.domain.user.repository;

import com.sparta.realtomatoapp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // 이메일로 사용자 조회
    Optional<User> findByUserName(String userName); // 사용자명으로 사용자 조회
}
