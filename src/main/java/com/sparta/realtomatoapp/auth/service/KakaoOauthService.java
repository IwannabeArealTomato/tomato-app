package com.sparta.realtomatoapp.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.realtomatoapp.auth.dto.OauthUserInfo;
import com.sparta.realtomatoapp.auth.entity.OauthUser;
import com.sparta.realtomatoapp.auth.entity.Provider;
import com.sparta.realtomatoapp.auth.repository.OauthUserRepository;
import com.sparta.realtomatoapp.security.util.PasswordEncoderUtil;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoOauthService {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final PasswordEncoderUtil passwordEncoder;
    private final UserRepository userRepository;
    private final OauthUserRepository oauthUserRepository;



    @Value("${kakao.client.id}")
    private String clientId;
    @Value("${kakao.redirect.uri}")
    private String redirectUri;


    public String kakaoLogin(String code) throws JsonProcessingException {
        String accessToken = getAccessToken(code);
        OauthUserInfo oauthUserInfo = getKakaoUserInfo(accessToken);
        Optional<OauthUser> existingOauthUser = oauthUserRepository.findByOauthIdAndProvider(
                oauthUserInfo.getOauthId(),
                oauthUserInfo.getProvider()
        );
        // 만약 이전에 카카오 OAUTH 로그인을 했던 기록이 있다면, Early Return
        if(existingOauthUser.isPresent()) return accessToken;

        // 첫 로그인 인데, 등록된 이메일이 없다면 회원 등록
        User user = userRepository.findByEmail(oauthUserInfo.getEmail()).orElseGet(
                () -> registerNewKakaoUser(oauthUserInfo)
        );

        // 카카오 oauth 계정 등록
        OauthUser kakaoUser = OauthUser.builder()
                .oauthId(oauthUserInfo.getOauthId())
                .provider(oauthUserInfo.getProvider())
                .user(user)
                .build();
        oauthUserRepository.save(kakaoUser);

        return accessToken;
    }

    private User registerNewKakaoUser(OauthUserInfo oauthUserInfo) {
        User newUser = User.builder()
                .email(oauthUserInfo.getEmail())
                .userName(oauthUserInfo.getNickname())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .role(UserRole.USER) // 최초 kakao 로그인 시 계정 일반 유저로 설정
                .address("주소를 변경해주세요.")
                .build();

        return userRepository.save(newUser);
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/token")
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
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private OauthUserInfo getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();
        String url = "https://kapi.kakao.com/v2/user/me";


        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> res = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

        JsonNode jsonNode = res.getBody();
        String oauthId = jsonNode.get("id").toString();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        return OauthUserInfo.builder()
                .oauthId(oauthId)
                .nickname(nickname)
                .email(email)
                .provider(Provider.KAKAO)
                .build();
    }
}
