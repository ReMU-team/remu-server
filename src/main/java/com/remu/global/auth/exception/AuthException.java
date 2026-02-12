package com.remu.global.auth.exception;

import com.remu.global.apiPayload.code.BaseErrorCode;
import com.remu.global.apiPayload.exception.GeneralException;

public class AuthException extends GeneralException {
    public AuthException(BaseErrorCode code) {
        super(code);
    }
}
