package com.remu.domain.feedback.exception.code;

import com.remu.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AiFeedbackSuccessCode implements BaseSuccessCode {

    CREATE(HttpStatus.OK,
            "REVIEW200_1",
            "성공적으로 피드백을 생성했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
