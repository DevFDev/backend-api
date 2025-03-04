package com.backend.devfordev.apiPayload.exception.handler;

import com.backend.devfordev.apiPayload.code.BaseErrorCode;

public class TeamHandler extends GeneralException {

    public TeamHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
