package com.remu.domain.user.exception;

import com.remu.global.apiPayload.code.BaseErrorCode;
import com.remu.global.apiPayload.exception.GeneralException;

public class UserException extends GeneralException {
    public UserException(BaseErrorCode code) {
        super(code);
    }
}
