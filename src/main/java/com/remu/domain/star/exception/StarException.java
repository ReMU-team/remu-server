package com.remu.domain.star.exception;

import com.remu.global.apiPayload.code.BaseErrorCode;
import com.remu.global.apiPayload.exception.GeneralException;

public class StarException extends GeneralException {
    public StarException(BaseErrorCode code) {
        super(code);
    }
}