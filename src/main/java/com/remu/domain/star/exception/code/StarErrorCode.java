package com.remu.domain.star.exception.code;

import com.remu.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StarErrorCode implements BaseErrorCode {

    STAR_NOT_FOUND(HttpStatus.NOT_FOUND,
            "STAR404_1",
            "해당 별을 찾을 수 없습니다."),
    GALAXY_NOT_FOUND(HttpStatus.NOT_FOUND,
            "STAR404_2",
            "해당 은하를 찾을 수 없습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}