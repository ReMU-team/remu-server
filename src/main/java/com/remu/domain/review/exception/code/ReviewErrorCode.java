package com.remu.domain.review.exception.code;

import com.remu.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements BaseErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND,
            "REVIEW404_1",
            "해당 회고를 찾지 못했습니다."),

    TRAVEL_NOT_FINISHED(HttpStatus.BAD_REQUEST,
            "REVIEW400_1",
            "해당 은하의 여행이 아직 끝나지 않았습니다."),

    ALREADY_EXISTS(HttpStatus.BAD_REQUEST,
            "REVIEW400_2",
            "이미 해당 다짐에 대한 회고가 존재합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
