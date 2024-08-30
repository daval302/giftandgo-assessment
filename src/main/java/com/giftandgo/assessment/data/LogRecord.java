package com.giftandgo.assessment.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class LogRecord {
    @Id
    private String uuid;
    private long timestamp;
    private long timeElapsed;
    private String requestUri;
    private int httpResponseCode;
    private String requestCountryCode;
    private String requestIpProvider;
    private String requestIpAddress;

    public LogRecord() {
    }

    public LogRecord(String uuid, long timestamp, long timeElapsed, String requestUri, int httpResponseCode, String requestCountryCode, String requestIpProvider, String requestIpAddress) {
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.timeElapsed = timeElapsed;
        this.requestUri = requestUri;
        this.httpResponseCode = httpResponseCode;
        this.requestCountryCode = requestCountryCode;
        this.requestIpProvider = requestIpProvider;
        this.requestIpAddress = requestIpAddress;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    public String getRequestCountryCode() {
        return requestCountryCode;
    }

    public void setRequestCountryCode(String requestCountryCode) {
        this.requestCountryCode = requestCountryCode;
    }

    public String getRequestIpProvider() {
        return requestIpProvider;
    }

    public void setRequestIpProvider(String requestIpProvider) {
        this.requestIpProvider = requestIpProvider;
    }

    public String getRequestIpAddress() {
        return requestIpAddress;
    }

    public void setRequestIpAddress(String requestIpAddress) {
        this.requestIpAddress = requestIpAddress;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        LogRecord logRecord = (LogRecord) object;
        return timestamp == logRecord.timestamp && timeElapsed == logRecord.timeElapsed && httpResponseCode == logRecord.httpResponseCode && Objects.equals(uuid, logRecord.uuid) && Objects.equals(requestUri, logRecord.requestUri) && Objects.equals(requestCountryCode, logRecord.requestCountryCode) && Objects.equals(requestIpProvider, logRecord.requestIpProvider) && Objects.equals(requestIpAddress, logRecord.requestIpAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
