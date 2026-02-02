package com.remu.domain.notification.controller;

import com.remu.domain.notification.converter.NotificationConverter;
import com.remu.domain.notification.dto.response.NotificationResponseDto;
import com.remu.domain.notification.entity.Notification;
import com.remu.domain.notification.exception.code.NotificationSuccessCode;
import com.remu.domain.notification.service.NotificationService;
import com.remu.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 목록 조회
    @GetMapping
    public ApiResponse<List<NotificationResponseDto>> getNotifications(@RequestParam("userId") Long userId) {
        // TODO: 실제로는 SecurityContext에서 userId를 가져와야 함
        List<Notification> notifications = notificationService.getNotifications(userId);
        List<NotificationResponseDto> response = notifications.stream()
                .map(NotificationConverter::toNotificationResponseDto)
                .collect(Collectors.toList());
        return ApiResponse.onSuccess(NotificationSuccessCode.NOTIFICATION_LIST_SEARCH, response);
    }

    // 알림 읽음 처리
    @PatchMapping("/{notificationId}/read")
    public ApiResponse<String> readNotification(@PathVariable("notificationId") Long notificationId) {
        notificationService.readNotification(notificationId);
        return ApiResponse.onSuccess(NotificationSuccessCode.NOTIFICATION_READ, "읽음 처리되었습니다.");
    }
}