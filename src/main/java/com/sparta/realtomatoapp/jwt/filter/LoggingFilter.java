package com.sparta.realtomatoapp.jwt.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "LoggingFilter") // 로그의 주제를 "LoggingFilter"로 설정
@Component // Spring에서 이 클래스를 빈으로 관리
@Order(1) // 필터 체인의 순서를 지정. 이 필터는 첫 번째로 실행됨
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request; // ServletRequest를 HttpServletRequest로 캐스팅
        String url = httpServletRequest.getRequestURI(); // 요청 URL을 가져옴
        log.info("Request URL: " + url); // 요청 URL을 로그로 출력
        chain.doFilter(request, response); // 다음 필터로 이동
        log.info("Response completed for URL: " + url); // 요청에 대한 응답이 완료되었음을 로그로 출력
    }
}
