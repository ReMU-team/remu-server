package com.remu.domain.place.exception.code;

import com.remu.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum PlaceSuccessCode implements BaseSuccessCode {
    PLACE_SEARCH_SUCCESS(HttpStatus.OK, "PLACE_200_1", "장소 검색에 성공했습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

}
