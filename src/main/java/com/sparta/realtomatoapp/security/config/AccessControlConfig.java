package com.sparta.realtomatoapp.security.config;
import com.sparta.realtomatoapp.security.Authorized;
import com.sparta.realtomatoapp.user.dto.AuthUser;
import com.sparta.realtomatoapp.user.entity.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AccessControlConfig {

    private final JwtProvider jwtProvider;

    @Before("@within(authorized) || @annotation(authorized)")
    private void roleCheck (JoinPoint joinPoint, Authorized authorized) {

        // 메서드 수준의 어노테이션이 없을 경우 클래스 수준 어노테이션 검사
        if (authorized == null) {
            Class<?> targetClass = joinPoint.getTarget().getClass();
            authorized = targetClass.getAnnotation(Authorized.class);
        }

        // 어노테이션이 없는 경우 실행 종료, memeberRole에 상관없이 접근가능한 API
        if (authorized == null) {
            return;
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String bearerToken = request.getHeader("Authorization");
        String token = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token =bearerToken.substring("Bearer ".length()); // "Bearer "의 길이가 7이므로 그 뒤의 문자열을 반환
        }

        AuthUser currentRequestAuthInfo = jwtProvider.getCurrentRequestAuthInfo(token);
        UserRole currentMemberRole = currentRequestAuthInfo.getRole();
        // 인증 로직 수행
        UserRole value = authorized.value();
        if (currentMemberRole!=value) {
            log.info("권한이 없는 요청입니다.");
            throw new RuntimeException("권한이 없는 요청입니다.");
        }
    }
}
