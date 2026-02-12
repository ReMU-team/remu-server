package com.remu.global.s3.exception;

import com.remu.global.apiPayload.code.BaseErrorCode;
import com.remu.global.apiPayload.exception.GeneralException;

public class S3Exception extends GeneralException {
    public S3Exception(BaseErrorCode code) {
        super(code);
    }
}

