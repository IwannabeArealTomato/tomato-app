package com.sparta.realtomatoapp.auth.repository;

import com.sparta.realtomatoapp.auth.entity.OauthUser;
import com.sparta.realtomatoapp.auth.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthUserRepository extends JpaRepository<OauthUser, Long> {
    Optional<OauthUser> findByOauthIdAndProvider(String oauthId, Provider provider);
}
