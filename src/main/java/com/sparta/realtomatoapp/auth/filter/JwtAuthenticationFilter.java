package com.sparta.realtomatoapp.auth.filter;

import com.sparta.realtomatoapp.auth.config.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
        if (requestURI.contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        //2. 토큰 검증
        String bearerToken = request.getHeader("Authorization");
        String token = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7); // "Bearer "의 길이가 7이므로 그 뒤의 문자열을 반환
        }

        if (!jwtProvider.verifyAccessToken(token)) {
            // 사용자에 잘못된 값 json으로 날림
            unAuthRespons(response);
            return;
        }

        filterChain.doFilter(request, response);

        //3. 권한 검증 ==>AOP 처리?
    }

    private void unAuthRespons(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
        response.setContentType("application/json"); // 응답 타입을 JSON으로 설정
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = "{\"error\": \"토큰이 만료되었습니다.\"}";
        response.getWriter().write(jsonResponse); // 응답 본문에 JSON 메시지 작성
    }
}
