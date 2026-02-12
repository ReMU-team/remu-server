package com.remu.domain.notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionDifficulty {
    EASY("쉬운 질문"),
    HARD("깊은 질문");

    private final String description;
}