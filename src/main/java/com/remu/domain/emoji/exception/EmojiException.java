package com.remu.domain.emoji.exception;

import com.remu.global.apiPayload.code.BaseErrorCode;
import com.remu.global.apiPayload.exception.GeneralException;

public class EmojiException extends GeneralException {
    public EmojiException(BaseErrorCode code) {
        super(code);
    }
}
