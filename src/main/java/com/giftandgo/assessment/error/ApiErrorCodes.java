package com.giftandgo.assessment.error;

public enum ApiErrorCodes {

    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error"),
    INVALID_REQUEST("INVALID_REQUEST", "Invalid request"),
    INVALID_PARAMETER("INVALID_PARAMETER", "Invalid parameter"),
    INVALID_IP("INVALID_IP", "Invalid IP address"),
    INVALID_RECORD("INVALID_RECORD", "Invalid record");

    private final String code;
    private final String description;

    ApiErrorCodes(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }


}
