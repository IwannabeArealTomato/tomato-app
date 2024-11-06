package com.sparta.realtomatoapp.security.exception.eunm;

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
    INVALID_EMAIL(HttpStatus.UNAUTHORIZED, "잘못된 이메일 입니다.", 401),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다.", 401),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "접근 권한 없음", 401),

    // 403 에러
    USER_NOT_FOUND(HttpStatus.FORBIDDEN, "존재하지 않는 유저", 403),
    INACTIVE_MEMBER(HttpStatus.FORBIDDEN, "탈퇴한 회원입니다.", 403),
    FORBIDDEN_STORE_ACCESS(HttpStatus.FORBIDDEN, "가게 관리 권한이 없습니다", 403),
    FORBIDDEN_MENU_ACCESS(HttpStatus.FORBIDDEN, "메뉴 관리 권한이 없습니다", 403),
    FORBIDDEN_ORDER_ACCESS(HttpStatus.FORBIDDEN, "주문 관리 권한이 없습니다", 403),

    // 404 에러
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 글", 404),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 가게", 404),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 메뉴", 404),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰", 404),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문", 404),


    // 409 에러
    ALREADY_EMAIL(HttpStatus.CONFLICT, "이미 사용되는 이메일", 409),
    ALREADY_NICKNAME(HttpStatus.CONFLICT, "이미 사용되는 닉네임", 409),
    ALREADY_NAMEING(HttpStatus.CONFLICT, "이미 사용되는 이름입니다",409);

    private final HttpStatus code;
    private final String message;
    private final int status;
}
