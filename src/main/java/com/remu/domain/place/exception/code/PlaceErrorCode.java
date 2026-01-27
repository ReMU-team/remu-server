package com.remu.domain.place.exception.code;

import com.remu.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PlaceErrorCode implements BaseErrorCode {

    // 400 Bad Request
    INVALID_PLACE_QUERY(HttpStatus.BAD_REQUEST, "PLACE_400_1", "검색어가 유효하지 않습니다."),

    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "PLACE_404_1", "검색 결과가 존재하지 않습니다."),

    // 500 Internal Server Error (Google API 관련)
    GOOGLE_API_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "PLACE_500_1", "구글 API 호출 중 내부 오류가 발생했습니다."),
    GOOGLE_API_KEY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PLACE_500_2", "구글 API 키 인증에 실패했습니다."),
    GOOGLE_API_OVER_QUERY_LIMIT(HttpStatus.INTERNAL_SERVER_ERROR, "PLACE_500_3", "구글 API 할당량을 초과했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
