package com.remu.domain.galaxy.exception.code;

import com.remu.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GalaxyErrorCode implements BaseErrorCode {

    INVALID_DATE_SEQUENCE(HttpStatus.BAD_REQUEST, "GALAXY_400_1", "날짜 순서가 올바르지 않습니다. (시작일 ≤ 도착일 ≤ 종료일)"),

    GALAXY_FORBIDDEN(HttpStatus.FORBIDDEN, "GALAXY_403_1", "해당 은하에 대한 접근 권한이 없습니다."),
    GALAXY_NOT_FOUND(HttpStatus.NOT_FOUND, "GALAXY_404_1", "해당 은하를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
