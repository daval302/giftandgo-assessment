package com.giftandgo.assessment.error.exception;

import com.giftandgo.assessment.error.ApiErrorCodes;

public class AppRestException extends RuntimeException {

    protected ApiErrorCodes errorCode;

    public AppRestException(String message, ApiErrorCodes errorCode) {
        super(message + (errorCode.getDescription().isEmpty() ? "" : " - " + errorCode.getDescription()));
        this.errorCode = errorCode;
    }

    public AppRestException(String message, ApiErrorCodes errorCode, Throwable cause) {
        super(message + (errorCode.getDescription().isEmpty() ? "" : " - " + errorCode.getDescription()), cause);
        this.errorCode = errorCode;
    }


    public ApiErrorCodes getErrorCode() {
        return errorCode;
    }
}
