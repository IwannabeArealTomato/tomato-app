package com.sparta.realtomatoapp.domain.user.repository;

import com.sparta.realtomatoapp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoy extends JpaRepository<User, Long> {
}
