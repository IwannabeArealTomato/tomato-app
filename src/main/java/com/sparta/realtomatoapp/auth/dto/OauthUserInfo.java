package com.sparta.realtomatoapp.auth.dto;

import com.sparta.realtomatoapp.auth.entity.Provider;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthUserInfo {
    private Long id;
    private String oauthId;
    private String nickname;
    private String email;
    private Provider provider;
}