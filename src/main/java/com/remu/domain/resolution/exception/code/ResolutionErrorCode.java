package com.remu.domain.resolution.exception.code;

import com.remu.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResolutionErrorCode implements BaseErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND,
            "RESOLUTION404_1",
            "해당 다짐을 찾지 못했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
