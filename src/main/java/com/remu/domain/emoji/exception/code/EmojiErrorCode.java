package com.remu.domain.emoji.exception.code;

import com.remu.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EmojiErrorCode implements BaseErrorCode {
    EMOJI_NOT_FOUND(HttpStatus.NOT_FOUND, "EMOJI_404_1", "요청하신 이모지를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
