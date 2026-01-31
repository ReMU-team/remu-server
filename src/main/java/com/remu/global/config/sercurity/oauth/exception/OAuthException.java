package com.remu.global.config.sercurity.oauth.exception;

import com.remu.global.apiPayload.code.BaseErrorCode;
import com.remu.global.apiPayload.exception.GeneralException;

public class OAuthException extends GeneralException {
    public OAuthException(BaseErrorCode code) {
        super(code);
    }
}
