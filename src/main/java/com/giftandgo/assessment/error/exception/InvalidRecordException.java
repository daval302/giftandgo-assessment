package com.giftandgo.assessment.error.exception;

import com.giftandgo.assessment.error.ApiErrorCodes;

import java.util.List;

public class InvalidRecordException extends AppRestException{
    private final List<String> errors;

    public InvalidRecordException(String message, List<String> errors) {
        super(message, ApiErrorCodes.INVALID_RECORD);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
