package com.jobis.tax.core.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServiceErrorType {
    CREATED(HttpStatus.CREATED, 1, "등록 되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 1, "비 인가 사용자입니다."),
    WAS_LOGOUT_USER(HttpStatus.BAD_REQUEST, 1, "로그아웃 된 사용자입니다."),
    INVALID_USER_TOKEN(HttpStatus.BAD_REQUEST, 1, "토큰의 유저 정보가 일치하지 않습니다."),
    INVALID_USER_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, 1, "리프레시 토큰이 유효하지 않습니다."),

    FORBIDDEN(HttpStatus.FORBIDDEN, 1, "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, 1, "해당 리소스를 찾을 수 없습니다."),
    FAILED_STATUS(HttpStatus.OK,1,"연결은 되지만 리소스 STATUS 상태값이 failed 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1, "시스템에 문제가 발생하였습니다."),
    INVALID_PARAMETER(HttpStatus.CONFLICT, 1, "유효하지 않은 전달값입니다."),

    INVALID_USER_NAME(HttpStatus.BAD_REQUEST, 1, "이름은 한글, 영문 대소문자만 허용합니다.."),
    INVALID_USER_NICKNAME(HttpStatus.BAD_REQUEST, 2, "별명은 영문 소문자만 허용합니다."),
    INVALID_USER_PASSWORD(HttpStatus.BAD_REQUEST, 3, "비밀번호는 영문 대문자, 영문 소문자, 특수 문자, 숫자 각 1개 이상씩 포함합니다."),
    INVALID_USER_PHONE_NUMBER(HttpStatus.BAD_REQUEST, 4, "휴대폰 번호는 숫자만 허용합니다."),
    INVALID_USER_REG_NO(HttpStatus.BAD_REQUEST, 4, "허용된 주민등록번호가 아닙니다."),

    FAILED_TO_ENCRYPT_DATA(HttpStatus.INTERNAL_SERVER_ERROR,1,"암호화 ENCRYPT 오류가 발생 하였습니다."),
    FAILED_TO_DECRYPT_DATA(HttpStatus.INTERNAL_SERVER_ERROR,1,"암호화 DECRYPT 오류가 발생 하였습니다.")

    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
