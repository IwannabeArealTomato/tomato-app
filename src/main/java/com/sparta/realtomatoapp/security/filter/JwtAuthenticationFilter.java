package com.sparta.realtomatoapp.security.filter;

import com.sparta.realtomatoapp.security.config.JwtProvider;
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

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");

        if (token != null && jwtProvider.verifyAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = request.getHeader("Refresh-Token");
        if (refreshToken != null && jwtProvider.verifyRefreshToken(refreshToken)) {
            String email = jwtProvider.getUserFromRefreshToken(refreshToken);
            String newAccessToken = jwtProvider.generateToken(email);

            response.setHeader("Authorization", newAccessToken);
            filterChain.doFilter(request, response);
        } else {
            unAuthResponse(response);
        }
    }

    private void unAuthResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = "{\"error\": \"토큰이 만료되었습니다.\"}";
        response.getWriter().write(jsonResponse);
    }
}
