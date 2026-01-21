package com.remu.domain.galaxy.exception;

import com.remu.global.apiPayload.code.BaseErrorCode;
import com.remu.global.apiPayload.exception.GeneralException;

public class GalaxyException extends GeneralException {
    public GalaxyException(BaseErrorCode code) {
        super(code);
    }
}
