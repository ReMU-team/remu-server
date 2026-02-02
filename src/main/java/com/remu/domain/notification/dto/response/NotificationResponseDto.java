package com.remu.domain.notification.dto.response;

import com.remu.domain.notification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private Long notificationId;
    private NotificationType type; // 알림 타입 (아이콘 구분용)
    private String content; // 알림 내용
    private Boolean isRead; // 읽음 여부
    private LocalDateTime createdAt; // 생성 시간
    private Long galaxyId; // 연결된 은하 ID (클릭 시 이동용)
}