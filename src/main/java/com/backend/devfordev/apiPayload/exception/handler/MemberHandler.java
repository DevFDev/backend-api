package com.backend.devfordev.apiPayload.exception.handler;

import com.backend.devfordev.apiPayload.code.BaseErrorCode;

public class MemberHandler extends GeneralException {

    public MemberHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
