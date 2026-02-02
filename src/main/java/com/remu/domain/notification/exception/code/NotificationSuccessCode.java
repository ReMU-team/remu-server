package com.remu.domain.notification.exception.code;

import com.remu.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotificationSuccessCode implements BaseSuccessCode {

    NOTIFICATION_LIST_SEARCH(HttpStatus.OK,
            "NOTI200_1",
            "알림 목록을 성공적으로 조회했습니다."),
    NOTIFICATION_READ(HttpStatus.OK,
            "NOTI200_2",
            "알림을 읽음 처리했습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}