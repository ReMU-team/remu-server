package com.remu.domain.user.exception.code;

import com.remu.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,
            "USER404_1",
            "해당 유저를 찾을 수 없습니다."),
    INVALID_NAME(HttpStatus.BAD_REQUEST,
            "USER400_1",
            "이름 형식이 올바르지 않습니다."),
    NAME_DUPLICATE(HttpStatus.BAD_REQUEST,
            "USER400_2",
            "이미 존재하는 이름입니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
