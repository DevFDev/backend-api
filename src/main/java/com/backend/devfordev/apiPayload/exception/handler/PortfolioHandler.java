package com.backend.devfordev.apiPayload.exception.handler;

import com.backend.devfordev.apiPayload.code.BaseErrorCode;

public class PortfolioHandler extends GeneralException{
    public PortfolioHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }

}
