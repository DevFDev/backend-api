package com.backend.devfordev.apiPayload.exception.handler;

import com.backend.devfordev.apiPayload.code.BaseErrorCode;

public class ProjectHandler extends GeneralException{
    public ProjectHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
