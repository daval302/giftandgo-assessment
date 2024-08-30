package com.giftandgo.assessment.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Log Rest Client requests
 */
@Component
public class RequestLoggingInterceptor implements ClientHttpRequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        return execution.execute(request, body);

    }

    private void logRequest(HttpRequest request, byte[] body) {
        logger.debug("Request URL: {}", request.getURI().toString());
        logger.debug("Request Method: {}", request.getMethod().name());
        logger.debug("Request Headers: {}", request.getHeaders());
        logger.debug("Request Body: {}", body != null ? new String(body) : "null");
    }
}
