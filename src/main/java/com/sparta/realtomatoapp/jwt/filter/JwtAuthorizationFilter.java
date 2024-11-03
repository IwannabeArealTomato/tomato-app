package com.sparta.realtomatoapp.jwt.filter;

import com.sparta.realtomatoapp.jwt.util.JwtUtil;
import com.sparta.realtomatoapp.security.UserDetails.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가") // 로그 주제를 "JWT 검증 및 인가"로 설정
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    // JwtAuthorizationFilter는 인증된 사용자만이 접근할 수 있는 요청에 대해 JWT 토큰을 검증하고,
    // 유효한 토큰일 경우 SecurityContextHolder에 사용자 정보를 설정합니다.

    private final JwtUtil jwtUtil; // JWT 유틸리티 클래스
    private final UserDetailsServiceImpl userDetailsService; // 사용자 정보 서비스

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil; // JWT 유틸리티 인스턴스 초기화
        this.userDetailsService = userDetailsService; // 사용자 정보 서비스 인스턴스 초기화
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.extractTokenFromRequest(req); // 요청에서 토큰 추출

        if (StringUtils.hasText(token) && jwtUtil.isValidToken(token)) { // 토큰이 유효한지 확인
            setAuthenticationContext(token); // 유효하면 인증 컨텍스트 설정
        }
        filterChain.doFilter(req, res); // 다음 필터로 이동
    }

    private void setAuthenticationContext(String token) {
        Claims claims = jwtUtil.getClaims(token); // 토큰에서 클레임 정보 추출
        String email = claims.getSubject(); // 클레임에서 이메일 정보 추출

        UserDetails userDetails = userDetailsService.loadUserByUsername(email); // 이메일을 통해 사용자 정보 로드
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // 인증 객체 생성
        SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContextHolder에 인증 객체 설정
    }
}
