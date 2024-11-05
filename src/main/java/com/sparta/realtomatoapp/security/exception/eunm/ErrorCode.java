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
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다.", 401),
    CANNOT_FRIEND_YOURSELF(HttpStatus.UNAUTHORIZED, "본인에게 친구 요청", 401),
    CANNOT_LIKE_YOURSELF(HttpStatus.UNAUTHORIZED, "본인게시물 혹은 댓글 좋아요", 401),
    UNAUTHORIZED_ACTION(HttpStatus.UNAUTHORIZED, "댓글을 편집할 권한이 없음", 401),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "권한 없음", 401), // 추가된 부분


    // 404 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저", 404),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글", 404),
    INACTIVE_MEMBER(HttpStatus.NOT_FOUND, "탈퇴한 회원입니다.", 404),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "친구 요청을 찾을 수 없습니다.", 404),
    NEWS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 뉴스", 404),  // 뉴스가 존재하지 않을 때
    FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 친구를 찾을 수 없습니다.", 404),

    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 좋아요", 404),


    // 409 에러

    ALREADY_EMAIL(HttpStatus.CONFLICT, "이미 사용되는 이메일", 409),
    ALREADY_FRIEND(HttpStatus.CONFLICT, "이미 친구 상태", 409),
    ALREADY_REQUEST(HttpStatus.CONFLICT, "이미 요청 상태", 409),
    ALREADY_NICKNAME(HttpStatus.CONFLICT, "이미 사용되는 닉네임", 409),
    ALREADY_LIKE(HttpStatus.CONFLICT, "이미 좋아요 상태", 409);

    private final HttpStatus code;
    private final String message;
    private final int status;
}
