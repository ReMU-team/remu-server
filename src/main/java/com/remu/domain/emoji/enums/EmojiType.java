package com.remu.domain.emoji.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmojiType {
    GALAXY, //은하
    REVIEW, // 회고
    RESOLUTION, // 다짐
    STAR, // 기록
    STAR_COLOR; // 별 카드 색상
}
