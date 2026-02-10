package com.remu.global.s3.exception.code;

import com.remu.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum S3SuccessCode implements BaseSuccessCode {

    // S3 성공 응답
    S3_UPLOAD_SUCCESS(HttpStatus.OK, "S3201", "파일 업로드가 성공적으로 완료되었습니다."),
    S3_DELETE_SUCCESS(HttpStatus.OK, "S3202", "파일이 성공적으로 삭제되었습니다."),
    S3_VIEW_SUCCESS(HttpStatus.OK, "S3203", "파일 조회용 URL 발급에 성공했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}