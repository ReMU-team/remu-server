package com.remu.domain.user.exception.code;

import com.remu.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserSuccessCode implements BaseSuccessCode {

    USER_FOUND(HttpStatus.FOUND,
            "USER302_1",
            "해당 유저를 찾았습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
