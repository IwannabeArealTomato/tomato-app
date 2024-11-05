package com.sparta.realtomatoapp.auth.controller.resolver;

import com.sparta.realtomatoapp.common.entity.LoginUser;
import com.sparta.realtomatoapp.security.config.JwtConfig;
import com.sparta.realtomatoapp.user.dto.AuthUser;
import com.sparta.realtomatoapp.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtConfig jwtConfig;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class) &&
                parameter.getParameterType().equals(AuthUser.class);
    }


    @Override
    public AuthUser resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getJwtAccessTokenSecretKey().getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token);

            Claims payload = claimsJws.getPayload();
            String email = payload.getSubject();
            UserRole role = UserRole.valueOf(payload.get("role", String.class));
            Long userId = payload.get("userId", Long.class);

            return AuthUser.builder()
                    .email(email)
                    .role(role)
                    .userId(userId)
                    .build();
        }
        return null;
    }
}
