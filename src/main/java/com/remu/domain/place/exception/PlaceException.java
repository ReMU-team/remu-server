package com.remu.domain.place.exception;

import com.remu.global.apiPayload.code.BaseErrorCode;
import com.remu.global.apiPayload.exception.GeneralException;

public class PlaceException extends GeneralException {
    public PlaceException(BaseErrorCode code) {
        super(code);
    }
}
