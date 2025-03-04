package com.backend.devfordev.apiPayload.exception.handler;

import com.backend.devfordev.apiPayload.code.BaseErrorCode;

public class CommunityHandler extends GeneralException {

    public CommunityHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
