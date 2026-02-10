package com.remu.global.s3.exception.code;

import com.remu.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum S3ErrorCode implements BaseErrorCode {

    // S3 관련 에러 (S3_5000번대)
    S3_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3501", "S3 파일 업로드에 실패했습니다."),
    S3_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3502", "S3 파일 삭제에 실패했습니다."),

    // 파일 검증 관련 (S3_4000번대)
    S3_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "S3401", "업로드할 파일이 존재하지 않습니다."),
    S3_INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, "S3402", "지원하지 않는 파일 형식입니다."),
    S3_FILE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "S3403", "파일 용량이 제한을 초과했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}