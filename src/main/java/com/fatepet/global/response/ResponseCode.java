package com.fatepet.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {

    // 200 OK
    SUCCESS(HttpStatus.OK, "요청이 성공했습니다."),
    CREATED(HttpStatus.CREATED, "리소스가 생성되었습니다."),
    DUPLICATE_CHECK_PASSED(HttpStatus.OK, "중복 검사를 통과했습니다."),

    // 400 BAD REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    DUPLICATE(HttpStatus.BAD_REQUEST, "중복된 값입니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    WRONG_USERNAME_PASSWORD(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
    WRONG_TOKEN(HttpStatus.FORBIDDEN, "유효하지 않은 토큰입니다."),

    // 404 NOT FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),

    // 500 INTERNAL SERVER ERROR
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ResponseCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getStatusCode() {
        return httpStatus.value();
    }
}
