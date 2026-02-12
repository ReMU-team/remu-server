package com.remu.domain.resolution.exception.code;

import com.remu.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResolutionSuccessCode implements BaseSuccessCode {

    FOUND(HttpStatus.OK,
            "RESOLUTION200_1",
            "성공적으로 다짐을 조회했습니다."),

    CREATE(HttpStatus.OK,
            "RESOLUTION200_2",
                    "성공적으로 다짐을 생성했습니다."),

    UPDATE(HttpStatus.OK,
            "RESOLUTION200_3",
            "성공적으로 다짐을 수정했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
