package com.remu.domain.star.exception.code;

import com.remu.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StarSuccessCode implements BaseSuccessCode {

    STAR_CREATED(HttpStatus.CREATED,
            "STAR201_1",
            "별을 성공적으로 생성했습니다."),
    STAR_UPDATED(HttpStatus.OK,
            "STAR200_3",
            "별을 성공적으로 수정했습니다."),
    STAR_LIST_SEARCH(HttpStatus.OK,
            "STAR200_1",
            "은하별 별 목록을 성공적으로 조회했습니다."),
    STAR_DETAIL_SEARCH(HttpStatus.OK,
            "STAR200_2",
            "별 상세 정보를 성공적으로 조회했습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}