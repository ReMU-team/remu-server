package com.remu.domain.notification.converter;

import com.remu.domain.notification.dto.response.NotificationResponseDto;
import com.remu.domain.notification.entity.Notification;

public class NotificationConverter {

    public static NotificationResponseDto toNotificationResponseDto(Notification notification) {
        return NotificationResponseDto.builder()
                .notificationId(notification.getId())
                .type(notification.getType())
                .content(notification.getContent())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .galaxyId(notification.getGalaxy().getId())
                .build();
    }
}