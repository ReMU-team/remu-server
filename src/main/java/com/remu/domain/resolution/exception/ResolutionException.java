package com.remu.domain.resolution.exception;

import com.remu.global.apiPayload.code.BaseErrorCode;
import com.remu.global.apiPayload.exception.GeneralException;

public class ResolutionException extends GeneralException {
    public ResolutionException(BaseErrorCode code) {
        super(code);
    }
}
