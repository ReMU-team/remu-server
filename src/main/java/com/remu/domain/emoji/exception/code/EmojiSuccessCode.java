package com.remu.domain.emoji.exception.code;

import com.remu.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EmojiSuccessCode implements BaseSuccessCode {
    // 이모지 목록 조회 성공
    EMOJI_FETCH_SUCCESS(HttpStatus.OK, "EMOJI_200_1", "이모지 목록을 성공적으로 불러왔습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
