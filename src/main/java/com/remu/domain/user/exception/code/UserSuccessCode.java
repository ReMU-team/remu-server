package com.remu.domain.user.exception.code;

import com.remu.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserSuccessCode implements BaseSuccessCode {

    USER_PROFILE_CREATED(HttpStatus.OK,
            "USER200_1",
            "유저 프로필을 성공적으로 생성/수정했습니다."),
    USER_PROFILE_SEARCH(HttpStatus.OK,
            "USER200_2",
            "유저 프로필을 성공적으로 조회했습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
