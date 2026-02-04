package com.remu.domain.notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    ARRIVAL("여행 도착"),
    RECORD("기록 유도"),
    QUESTION("랜덤 질문"),
    REVIEW("회고 유도");

    private final String description;
}