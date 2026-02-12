package com.remu.domain.feedback.exception.code;

import com.remu.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AiFeedbackSuccessCode implements BaseSuccessCode {

    FOUND(HttpStatus.OK,
            "AiFeedback200_1",
            "성공적으로 피드백을 조회했습니다."),

    CREATE(HttpStatus.OK,
            "AiFeedback_2",
            "성공적으로 피드백을 생성했습니다."),

    UPDATE(HttpStatus.OK,
            "AiFeedback_3",
                    "성공적으로 피드백을 수정했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
