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

        String accessToken = extractToken(request.getHeader("Authorization"));
        if (accessToken != null && jwtProvider.verifyAccessToken(accessToken)) {
            log.info("Access Token verified successfully");
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = request.getHeader("Refresh-Token");
        if (refreshToken != null && jwtProvider.verifyRefreshToken(refreshToken)) {
            String username = jwtProvider.getUserFromRefreshToken(refreshToken);
            String newAccessToken = jwtProvider.generateAccessToken(username);

            response.setHeader("Authorization", "Bearer " + newAccessToken);
            log.info("New Access Token generated and set in header");
            filterChain.doFilter(request, response);
        } else {
            log.warn("Access or Refresh token verification failed.");
            unAuthResponse(response);
            return;
        }
    }


    private String extractToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (token.split("\\.").length == 3) { // JWT 형식 확인
                log.info("Extracted token: {}", token);
                return token;
            } else {
                log.error("Invalid token format detected in Authorization header: {}", token);
            }
        } else {
            log.warn("No Bearer token found or improper format in Authorization header: {}", bearerToken);
        }
        return null;
    }


    private void unAuthResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = "{\"error\": \"토큰이 만료되었거나 유효하지 않습니다.\"}";
        response.getWriter().write(jsonResponse);
    }
}
