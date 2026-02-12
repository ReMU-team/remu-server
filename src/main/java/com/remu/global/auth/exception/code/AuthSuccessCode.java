package com.remu.global.auth.exception.code;

import com.remu.global.apiPayload.code.BaseSuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthSuccessCode implements BaseSuccessCode {

    TOKEN_REISSUE_SUCCESS(HttpStatus.OK,
            "AUTH200_1",
            "토큰이 재발급 되었습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK,
            "AUTH200_2",
            "로그아웃 되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK,
            "AUTH200_3",
            "로그인 성공.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
