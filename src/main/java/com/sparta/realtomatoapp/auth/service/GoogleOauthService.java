package com.sparta.realtomatoapp.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.sparta.realtomatoapp.auth.dto.OauthLoginResponseDto;
import com.sparta.realtomatoapp.auth.dto.OauthUserInfo;
import com.sparta.realtomatoapp.auth.entity.OauthUser;
import com.sparta.realtomatoapp.auth.entity.Provider;
import com.sparta.realtomatoapp.auth.repository.OauthUserRepository;
import com.sparta.realtomatoapp.security.config.JwtProvider;
import com.sparta.realtomatoapp.security.refreshToken.entity.RefreshToken;
import com.sparta.realtomatoapp.security.refreshToken.repository.RefreshTokenRepository;
import com.sparta.realtomatoapp.security.util.PasswordEncoderUtil;
import com.sparta.realtomatoapp.user.dto.AuthUser;
import com.sparta.realtomatoapp.user.entity.User;
import com.sparta.realtomatoapp.user.entity.UserRole;
import com.sparta.realtomatoapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleOauthService {

    private final RestTemplate restTemplate;
    private final PasswordEncoderUtil passwordEncoder;
    private final UserRepository userRepository;
    private final OauthUserRepository oauthUserRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecretId;

    @Value("${google.redirect.uri}")
    private String redirectUri;


    public OauthLoginResponseDto googleLogin(String code) throws JsonProcessingException {
        String accessToken = getAccessToken(code);
        OauthUserInfo oauthUserInfo = getGoogleUserInfo(accessToken);
        Optional<OauthUser> existingUserInfo = oauthUserRepository.findByOauthIdAndProvider(
                oauthUserInfo.getOauthId(),
                oauthUserInfo.getProvider()
        );
        // 만약 이전에 구글 OAUTH 로그인을 했던 기록이 있다면
        User savedUser;
        if(existingUserInfo.isPresent()) {
            savedUser = existingUserInfo.get().getUser();
        } else {
            // 첫 로그인 인데, 등록된 이메일이 없다면 회원 등록
            savedUser = userRepository.findByEmail(oauthUserInfo.getEmail()).orElseGet(
                    () -> registerNewGoogleUser(oauthUserInfo)
            );

            // 구글 oauth 계정 등록
            OauthUser googleUser = OauthUser.builder()
                    .oauthId(oauthUserInfo.getOauthId())
                    .provider(oauthUserInfo.getProvider())
                    .user(savedUser)
                    .build();
            oauthUserRepository.save(googleUser);
        }

        // JWT 토큰 생성을 위한 AuthUser 객체 생성
        AuthUser authUser = AuthUser.builder()
                .userId(savedUser.getUserId())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();

        String jwtAccessToken = jwtProvider.createJwtToken(authUser);
        String jwtRefreshToken = jwtProvider.createRefreshToken(authUser);

        // DB에 리프레시 토큰 저장
        RefreshToken refreshEntity = RefreshToken.builder()
                .tokenValue(jwtRefreshToken)
                .user(savedUser)
                .build();

        RefreshToken existingRefreshToken = refreshTokenRepository.findByUser(savedUser).orElseGet(() ->
                refreshTokenRepository.save(refreshEntity));

        // 발급 해준 토큰이 있을 시 -> 변경
        existingRefreshToken.updateToken(jwtRefreshToken);
        refreshTokenRepository.save(existingRefreshToken);


        return OauthLoginResponseDto.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://oauth2.googleapis.com/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecretId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<Map> response = restTemplate.exchange(
                requestEntity,
                Map.class
        );

        // 응답받은 데이터들 추출
        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null) {
            throw new IllegalArgumentException("구글 엑세스 토큰 요청에 대한 응답이 비어있습니다.");
        }

        String accessToken = (String) responseBody.get("access_token");
        String refreshToken = (String) responseBody.get("refresh_token"); // refresh token에 활용
        Integer expiresIn = (Integer) responseBody.get("expires_in"); // 토큰 생명주기

        return accessToken;
    }

    private OauthUserInfo getGoogleUserInfo(String accessToken) throws JsonProcessingException {
        // 요청 URL 만들기
        String url = "https://www.googleapis.com/oauth2/v2/userinfo";

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> res = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

        JsonNode jsonNode = res.getBody();
        String oauthId = jsonNode.get("id").asText();
        String email = jsonNode.get("email").asText();
        String name = jsonNode.get("name").asText();

        return OauthUserInfo.builder()
                .oauthId(oauthId)
                .nickname(name)
                .email(email)
                .provider(Provider.GOOGLE)
                .build();
    }
    private User registerNewGoogleUser(OauthUserInfo oauthUserInfo) {
        User newUser = User.builder()
                .email(oauthUserInfo.getEmail())
                .userName(oauthUserInfo.getNickname())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .role(UserRole.USER) // 최초 google 로그인 시 계정 일반 유저로 설정
                .address("주소를 변경해주세요.")
                .build();

        return userRepository.save(newUser);
    }
}
