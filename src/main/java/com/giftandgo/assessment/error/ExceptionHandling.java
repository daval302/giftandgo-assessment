package com.giftandgo.assessment.error;

import com.giftandgo.assessment.error.exception.AppRestException;
import com.giftandgo.assessment.error.exception.InvalidIpException;
import com.giftandgo.assessment.error.exception.InvalidRecordException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class ExceptionHandling {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHandling.class);


    @ExceptionHandler(AppRestException.class)
    public Map<String, Object> handleAppRestException(final AppRestException ex, final HttpServletRequest request) {
        logger.error("AppRestException: " + ex.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request, ex.getMessage(), ex.getErrorCode());
    }

    @ExceptionHandler(RestClientException.class)
    public Map<String, Object> handleRestClientException(final RestClientException ex, final HttpServletRequest request) {
        logger.error("RestClientException: " + ex.getMessage());
        return buildResponseBody(HttpStatus.SERVICE_UNAVAILABLE, request, ex.getMessage());
    }

    @ExceptionHandler(InvalidIpException.class)
    public Map<String, Object> handleInvalidIpException(final InvalidIpException ex, final HttpServletRequest request) {
        logger.error("InvalidIpException: " + ex.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, request, ex.getMessage(), ex.getErrorCode());
    }

    @ExceptionHandler(InvalidRecordException.class)
    public Map<String, Object> handleInvalidRecordException(final InvalidRecordException ex, final HttpServletRequest request) {
        logger.error("InvalidRecordException: " + ex.getMessage());
        Map<String, Object> body = buildErrorResponse(HttpStatus.BAD_REQUEST, request, ex.getMessage(), ex.getErrorCode());
        body.put("errors", ex.getErrors());
        return body;
    }

    private Map<String, Object> buildErrorResponse(final HttpStatus httpStatus, final HttpServletRequest request, final String message, ApiErrorCodes errorCode) {
        Map<String, Object> body = buildResponseBody(httpStatus, request, message);
        body.put("error", httpStatus.getReasonPhrase());
        body.put("error_code", errorCode.name());
        return body;
    }

    private Map<String, Object> buildResponseBody(final HttpStatus httpStatus, final HttpServletRequest request, final String message) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("uuid", request.getAttribute("uuid"));
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", httpStatus.value());
        body.put("message", message);
        body.put("path", request.getRequestURI());
        return body;
    }


}
