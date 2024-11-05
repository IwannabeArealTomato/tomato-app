package com.sparta.realtomatoapp.auth.controller.resolver;

import com.sparta.realtomatoapp.common.entity.LoginUser;
import com.sparta.realtomatoapp.user.dto.AuthUser;
import com.sparta.realtomatoapp.security.config.JwtProvider;
import com.sparta.realtomatoapp.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class) &&
                parameter.getParameterType().equals(AuthUser.class);
    }

    @Override
    public AuthUser resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            // JWT 토큰 검증
            if (jwtProvider.verifyAccessToken(token)) {
                Claims claims = jwtProvider.getClaimsFromToken(token, true); // Access Token의 경우 true 전달

                String email = claims.getSubject();
                UserRole role = UserRole.valueOf(claims.get("role", String.class));
                Long userId = claims.get("userId", Long.class);

                return AuthUser.builder()
                        .email(email)
                        .role(role)
                        .userId(userId)
                        .build();
            }
        }
        return null; // 토큰이 없거나 유효하지 않은 경우 null 반환
    }
}
