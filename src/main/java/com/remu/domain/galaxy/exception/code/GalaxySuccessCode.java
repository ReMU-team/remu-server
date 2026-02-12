package com.remu.domain.galaxy.exception.code;

import com.remu.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GalaxySuccessCode implements BaseSuccessCode {
    // 201 Created와 200 OK
    // 은하 생성 성공
    GALAXY_CREATED(HttpStatus.CREATED, "GALAXY201", "새로운 은하가 성공적으로 생성되었습니다."),
    // 은하 목록/상세 조회 성공
    GALAXY_GET_SUCCESS(HttpStatus.OK, "GALAXY200_1", "은하 정보를 성공적으로 불러왔습니다."),
    GALAXY_LIST_GET_SUCCESS(HttpStatus.OK, "GALAXY200_2", "은하 목록을 성공적으로 조회했습니다."),
    // 은하 정보 수정 성공
    GALAXY_UPDATE_SUCCESS(HttpStatus.OK, "GALAXY200_3", "은하 정보가 성공적으로 수정되었습니다."),
    // 은하 삭제 성공
    GALAXY_DELETE_SUCCESS(HttpStatus.OK, "GALAXY200_4", "은하가 성공적으로 삭제되었습니다.");
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
