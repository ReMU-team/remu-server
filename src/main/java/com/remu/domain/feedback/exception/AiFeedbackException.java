package com.remu.domain.feedback.exception;

import com.remu.global.apiPayload.code.BaseErrorCode;
import com.remu.global.apiPayload.exception.GeneralException;

public class AiFeedbackException extends GeneralException {
    public AiFeedbackException(BaseErrorCode code) { super(code); }
}
