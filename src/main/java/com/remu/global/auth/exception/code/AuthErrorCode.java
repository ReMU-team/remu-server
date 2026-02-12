package com.remu.global.auth.exception.code;

import com.remu.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,
            "AUTH401_1",
            "인증이 필요합니다."),

    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED,
            "AUTH401_2",
            "리프레시 토큰이 유효하지 않습니다."),

    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,
            "AUTH401_3",
            "리프레시 토큰이 만료되었습니다."),

    REFRESH_TOKEN_TAMPERED(HttpStatus.UNAUTHORIZED,
            "AUTH401_4",
            "리프레시 토큰이 위조되었습니다."),

    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND,
            "AUTH404_1",
            "리프레시 토큰을 찾을 수 없습니다."),

    INVALID_KAKAO_TOKEN(HttpStatus.UNAUTHORIZED,
            "AUTH401_5",
            "유효하지 않은 카카오 토큰입니다."),

    KAKAO_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,
            "AUTH500_1",
            "카카오 서버 통신 중 에러가 발생했습니다."),

    INVALID_PROVIDER(HttpStatus.BAD_REQUEST,
            "AUTH400_1",
            "지원하지 않는 플랫폼입니다."),

    INVALID_GOOGLE_TOKEN(HttpStatus.UNAUTHORIZED,
            "AUTH401_6",
            "유효하지 않은 구글 토큰입니다."),

    GOOGLE_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,
            "AUTH500_2",
            "구글 인증 서버 통신 중 에러가 발생했습니다."),

    INVALID_APPLE_TOKEN(HttpStatus.UNAUTHORIZED,
            "AUTH401_7",
            "유효하지 않은 애플 토큰입니다."),

    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,
            "AUTH401_8",
            "Access token이 만료되었습니다."),

    ACCESS_TOKEN_INVALID(HttpStatus.UNAUTHORIZED,
            "AUTH401_9",
            "유효하지 않은 access token 입니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
