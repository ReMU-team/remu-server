package com.remu.global.auth.exception.code;

import com.remu.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,
            "AUTH401_0",
            "인증이 필요합니다."),

    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED,
            "AUTH401_1",
            "리프레시 토큰이 유효하지 않습니다."),

    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,
            "AUTH401_2",
            "리프레시 토큰이 만료되었습니다."),

    REFRESH_TOKEN_TAMPERED(HttpStatus.UNAUTHORIZED,
            "AUTH401_3",
            "리프레시 토큰이 위조되었습니다."),

    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND,
            "AUTH404_1",
            "리프레시 토큰을 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
