package com.giftandgo.assessment.error.exception;

import com.giftandgo.assessment.error.ApiErrorCodes;

public class InvalidIpException extends AppRestException{
    private final String ipAddress;

    public InvalidIpException(String message, String ipAddress) {
        super(message, ApiErrorCodes.INVALID_IP);
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
