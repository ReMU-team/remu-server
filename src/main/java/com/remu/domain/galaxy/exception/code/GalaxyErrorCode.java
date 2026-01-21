package com.remu.domain.galaxy.exception.code;

import com.remu.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GalaxyErrorCode implements BaseErrorCode {
    INVALID_DATE_SEQUENCE(HttpStatus.BAD_REQUEST, "GALAXY_400_1", "날짜 순서가 올바르지 않습니다. (시작일 ≤ 도착일 ≤ 종료일)");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
