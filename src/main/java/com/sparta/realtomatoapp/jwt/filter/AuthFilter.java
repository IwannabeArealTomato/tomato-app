package com.sparta.realtomatoapp.jwt.filter;

import com.sparta.realtomatoapp.jwt.util.JwtUtil;
import com.sparta.realtomatoapp.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j(topic = "AuthFilter") // 로그의 주제를 "AuthFilter"로 설정
@Component // Spring이 관리하는 컴포넌트로 등록
@Order(2) // 필터 체인에서 이 필터의 순서를 지정
public class AuthFilter implements Filter {

    private final UserRepository userRepository; // 사용자 정보를 조회하기 위한 UserRepository
    private final JwtUtil jwtUtil; // JWT 유틸리티 클래스 인스턴스

    public AuthFilter(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository; // UserRepository 인스턴스 초기화
        this.jwtUtil = jwtUtil; // JwtUtil 인스턴스 초기화
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request; // HttpServletRequest로 캐스팅
        String url = httpServletRequest.getRequestURI(); // 요청 URL을 가져옴

        if (isPublicUrl(url)) { // 요청 URL이 공개 URL인지 확인
            chain.doFilter(request, response); // 인증 없이 다음 필터로 이동
            return;
        }

        String token = jwtUtil.extractTokenFromRequest(httpServletRequest); // 요청에서 토큰을 추출
        if (token != null && jwtUtil.isValidToken(token)) { // 토큰이 유효한지 확인
            authenticateUserFromToken(token); // 유효한 토큰일 경우 사용자 인증 수행
        }
        chain.doFilter(request, response); // 요청을 다음 필터로 전달
    }

    private boolean isPublicUrl(String url) {
        return StringUtils.hasText(url) &&
                (url.startsWith("/api/user") || url.startsWith("/css") || url.startsWith("/js")); // 공개 URL 경로 확인
    }

    private void authenticateUserFromToken(String token) {
        Claims claims = jwtUtil.getClaims(token); // 토큰에서 클레임 정보 추출
        String email = claims.getSubject(); // 클레임에서 이메일 정보 추출
        userRepository.findByEmail(email).ifPresent(user -> log.info("사용자 인증 완료: " + user.getEmail())); // 사용자 인증 완료 로그 출력
    }
}
