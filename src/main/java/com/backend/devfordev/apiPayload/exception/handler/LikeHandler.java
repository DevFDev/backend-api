package com.backend.devfordev.apiPayload.exception.handler;

import com.backend.devfordev.apiPayload.code.BaseErrorCode;

public class LikeHandler extends GeneralException {

    public LikeHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
