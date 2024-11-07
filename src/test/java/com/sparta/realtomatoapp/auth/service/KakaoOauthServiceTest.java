package com.sparta.realtomatoapp.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.sparta.realtomatoapp.auth.dto.LoginTokenResponseDto;
import com.sparta.realtomatoapp.auth.dto.OauthLoginResponseDto;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) // 단위 테스트
class KakaoOauthServiceTest {

    // DI가 필요한 객체들
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private PasswordEncoderUtil passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OauthUserRepository oauthUserRepository;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private KakaoOauthService kakaoOauthService;

    @BeforeEach
    void kakaoLoginSetUp() throws JsonProcessingException {
        // getAccessToken()의 restTemplate.exchange 메서드에 대한 stubbing
        String fakeResponse = "Fake Response with access token";
        ResponseEntity<String> mockResponseEntity = ResponseEntity.ok(fakeResponse);
        when(restTemplate.exchange(
                any(RequestEntity.class),
                eq(String.class)
        )).thenReturn(mockResponseEntity);

        // getAccessToken()의 objectMapper.readTree 메서드에 대한 stubbing
        JsonNode fakeJsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(fakeResponse)).thenReturn(fakeJsonNode);
        when(fakeJsonNode.get("access_token")).thenReturn(mock(JsonNode.class));
        when(fakeJsonNode.get("access_token").asText()).thenReturn("fake-access-token-12345");


        // getKakaoUserInfo에서 "properties"와 "kakao_account"를 반환하도록 설정
        JsonNode mockPropertiesNode = mock(JsonNode.class);
        JsonNode mockKakaoAccountNode = mock(JsonNode.class);
        when(fakeJsonNode.get("id")).thenReturn(new TextNode("카카오고유아이디값"));
        when(fakeJsonNode.get("properties")).thenReturn(mockPropertiesNode);
        when(mockPropertiesNode.get("nickname")).thenReturn(new TextNode("카카오맨"));
        when(fakeJsonNode.get("kakao_account")).thenReturn(mockKakaoAccountNode);
        when(mockKakaoAccountNode.get("email")).thenReturn(new TextNode("fake-email@kakao.com"));
        ResponseEntity<JsonNode> mockJsonNodeResponseEntity = mock(ResponseEntity.class);
        when(mockJsonNodeResponseEntity.getBody()).thenReturn(fakeJsonNode);
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(JsonNode.class)
        )).thenReturn(mockJsonNodeResponseEntity);
    }


    @Test
    @DisplayName("기존 Oauth로 로그인 한 기록이 있는 유저는 회원가입 없이 토큰만 발급 테스트")
    void kakaoOauthLoginTest() throws JsonProcessingException {
        // given
        User registeredUser = User.builder()
                .userId(100L)
                .userName("엉엉")
                .email("abc@kakao.com")
                .role(UserRole.USER)
                .address("한국")
                .password("1234")
                .build();

        OauthUser mockOauthUser = OauthUser.builder()
                .id(1L)
                .oauthId("카카오고유아이디값")
                .provider(Provider.KAKAO)
                .user(registeredUser)
                .build();
        RefreshToken mockRefreshToken = RefreshToken.builder()
                .id(1L)
                .user(registeredUser)
                .tokenValue("mock-jwt-refresh-token")
                .build();

        when(oauthUserRepository.findByOauthIdAndProvider(
                any(),
                any()
        )).thenReturn(Optional.of(mockOauthUser));

        when(jwtProvider.createJwtToken(any(AuthUser.class))).thenReturn("mock-jwt-token");
        when(jwtProvider.createRefreshToken(any(AuthUser.class))).thenReturn("mock-jwt-refresh-token");

        when(refreshTokenRepository.findByUser(any(User.class))).thenReturn(Optional.empty());

        // when
        OauthLoginResponseDto Tokens = kakaoOauthService.kakaoLogin("fake-code");

        // then
        assertThat(Tokens).isNotNull();
        assertThat(Tokens.getAccessToken()).isEqualTo("mock-jwt-token");
        assertThat(Tokens.getRefreshToken()).isEqualTo("mock-jwt-refresh-token");
        verify(oauthUserRepository, times(0)).save(any());
        // spy
    }

    // @DisplayName("기존 Oauth로 로그인 한 기록은 없지만, 회원가입이 필요한 케이스")
    // @DisplayName("기존 Oauth로 로그인 한 기록은 없지만, 해당 이메일로 가입이 이미 되어있던 케이스")
}