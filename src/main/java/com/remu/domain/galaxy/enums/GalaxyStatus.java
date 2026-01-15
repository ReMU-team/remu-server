package com.remu.domain.galaxy.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GalaxyStatus {
    READY("여행 전"),
    ONGOING("여행 중"),
    COMPLETED("여행 완료");

    private final String description;
}
