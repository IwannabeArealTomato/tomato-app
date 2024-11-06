package com.sparta.realtomatoapp.security.filter;

import com.sparta.realtomatoapp.security.config.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //AdminTokenInterceptor는 관리자 권한을 가진 사용자가 접근할 수 있는 특정 경로에서 추가적인 인증 절차를 통해 보안을 강화함

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1. 토큰이 필요없는 API
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/api/auth") || requestURI.contains("/refresh-token")) {
            filterChain.doFilter(request, response);
            return;
        }

        //2. 엑세스 토큰 검증
        String bearerToken = request.getHeader("Authorization");
        String token = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring("Bearer ".length()); // "Bearer "의 길이가 7이므로 그 뒤의 문자열을 반환
        } else {
            // 사용자에 잘못된 값 json으로 날림
            unAuthResponse(response);
            return;
        }

        // 3. 엑세스 토큰이 유효하다면 다음 필터 수행
        if (jwtProvider.verifyAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4. 엑세스 토큰이 만료 되었을 경우. 쿠키에 저장된 리프레시 토큰 검증
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            refreshToken = Arrays.stream(cookies)
                    .filter(cookie -> "refresh_token".equals(cookie.getName()))
                    .map(Cookie::getValue) // 쿠키의 값을 추출
                    .findFirst() // 첫 번째 매칭되는 값을 찾음
                    .orElse(null);
        }
        if (jwtProvider.verifyRefreshToken(refreshToken)) {
            refreshResponse(response);
            return;
        }

        // 사용자에 잘못된 값 json으로 날림
        unAuthResponse(response);
        return;

        //5. 권한 검증 ==>AOP 처리?
    }

    private void unAuthResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
        response.setContentType("application/json"); // 응답 타입을 JSON으로 설정
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = "{\"error\": \"토큰이 만료되었거나 잘못된 형식입니다.\"}";
        response.getWriter().write(jsonResponse); // 응답 본문에 JSON 메시지 작성
    }

    private void refreshResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
        response.setContentType("application/json"); // 응답 타입을 JSON으로 설정
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = "{"
                + "\"message\": \"엑세스 토큰이 만료되었습니다. 리프레시 토큰을 사용해 새로운 엑세스 토큰을 발급받으세요.\","
                + "\"refreshTokenUrl\": \"/refresh-token\""
                + "}";
        response.getWriter().write(jsonResponse); // 응답 본문에 JSON 메시지 작성
    }
}
