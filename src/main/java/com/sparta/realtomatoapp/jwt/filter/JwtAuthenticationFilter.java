package com.sparta.realtomatoapp.jwt.filter;

import com.sparta.realtomatoapp.jwt.util.JwtUtil;
import com.sparta.realtomatoapp.security.UserDetailsImpl;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // JwtAuthenticationFilter는 로그인 요청 시 인증을 수행하고,
    // 성공적으로 인증된 사용자를 대상으로 JWT를 생성해 응답 헤더에 추가하는 필터입니다.

    private final JwtUtil jwtUtil; // JWT 유틸리티 클래스 인스턴스

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil; // JWT 유틸리티 클래스 초기화
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Object principal = authResult.getPrincipal(); // 인증된 사용자 정보를 가져옴

        if (principal instanceof UserDetailsImpl) { // 사용자 정보가 UserDetailsImpl 인스턴스인지 확인
            UserDetailsImpl userDetails = (UserDetailsImpl) principal; // UserDetailsImpl로 캐스팅
            String accessToken = jwtUtil.createToken(userDetails.getUsername(), userDetails.getRole(), false); // 액세스 토큰 생성
            String refreshToken = jwtUtil.createToken(userDetails.getUsername(), userDetails.getRole(), true); // 리프레시 토큰 생성

            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.BEARER_PREFIX + accessToken); // 액세스 토큰을 응답 헤더에 추가
            response.addHeader(JwtUtil.REFRESH_TOKEN_HEADER, refreshToken); // 리프레시 토큰을 응답 헤더에 추가
            log.info("사용자 인증 성공: " + userDetails.getUsername()); // 인증 성공 로그 출력
        } else {
            log.error("인증 실패: Principal이 UserDetailsImpl 인스턴스가 아닙니다. 실제 타입: " + principal.getClass().getName()); // 인증 실패 로그
            throw new ClassCastException("Principal이 예상된 UserDetailsImpl 타입이 아닙니다."); // ClassCastException 예외 발생
        }

        super.successfulAuthentication(request, response, chain, authResult); // 상위 클래스의 인증 성공 메서드 호출
    }
}
