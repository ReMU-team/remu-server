package com.remu.domain.feedback.enums;

public enum AiFeedbackStatus {
    PROCESSING, // AI 분석 진행 중
    SUCCESS,    // 분석 완료 및 저장 완료
    FAILED  // AI 호출 실패
}
