package com.sparta.realtomatoapp.common.exception.eunm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 에러
    NOT_NULL(HttpStatus.BAD_REQUEST, "필수 값 누락", 400),
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "토큰이 존재하지 않음", 400),

    // 401 에러
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인 실패", 401),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰", 401),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다.", 401),
    UNAUTHORIZED_ACTION(HttpStatus.UNAUTHORIZED, "권한이 없음", 401),

    // 404 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저", 404),  // 추가된 부분

    // 409 에러
    ALREADY_EMAIL(HttpStatus.CONFLICT, "이미 사용되는 이메일", 409),
    ALREADY_FRIEND(HttpStatus.CONFLICT, "이미 친구 상태", 409),
    ALREADY_REQUEST(HttpStatus.CONFLICT, "이미 요청 상태", 409),
    ALREADY_NICKNAME(HttpStatus.CONFLICT, "이미 사용되는 닉네임", 409);

    private final HttpStatus code;
    private final String message;
    private final int status;
}
