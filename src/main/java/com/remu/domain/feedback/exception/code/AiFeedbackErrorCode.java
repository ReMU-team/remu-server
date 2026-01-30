package com.remu.domain.feedback.exception.code;

import com.remu.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AiFeedbackErrorCode implements BaseErrorCode {

    ALREADY_EXISTS(HttpStatus.BAD_REQUEST,
            "REVIEW400_1",
            "이미 해당 여행에 대한 피드백이 존재합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
