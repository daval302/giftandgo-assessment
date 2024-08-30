package com.giftandgo.assessment.component;

import com.giftandgo.assessment.data.GeolocationResponse;
import com.giftandgo.assessment.data.LogRecord;
import com.giftandgo.assessment.error.exception.InvalidIpException;
import com.giftandgo.assessment.repository.LogRecordRepository;
import com.giftandgo.assessment.service.IpInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class IpCheckInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(IpCheckInterceptor.class);
    private final String[] blacklistedCountryCodes = new String[]{"CN", "ES", "US"};
    private final Pattern pattern = Pattern.compile("^((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])$");
    private final IpInfoService ipInfoService;

    private final LogRecordRepository logRecordRepository;

    public IpCheckInterceptor(IpInfoService ipInfoService, LogRecordRepository logRecordRepository) {
        this.ipInfoService = ipInfoService;
        this.logRecordRepository = logRecordRepository;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String uuid = UUID.randomUUID().toString();
        request.setAttribute("uuid", uuid);
        request.setAttribute("timestamp", System.currentTimeMillis());

        String ipAddress = getClientIp(request);
        request.setAttribute("ipAddress", ipAddress);
        logger.info("Checking IP: " + ipAddress);

        if (!pattern.matcher(ipAddress).matches()) {
            logger.error("Invalid IP address: " + ipAddress);
            throw new InvalidIpException("Regex validation failed for IP: " + ipAddress, ipAddress);
        }

        GeolocationResponse geolocationResponse = ipInfoService.getGeolocationInfo(ipAddress);
        if (geolocationResponse != null) {
            String countryCode = geolocationResponse.countryCode();
            for (String blacklistedCountryCode : blacklistedCountryCodes) {
                if (blacklistedCountryCode.equals(countryCode)) {
                    logger.error("Blacklisted country code detected: " + countryCode);
                    throw new InvalidIpException("Blacklisted country code detected: " + countryCode, ipAddress);
                }
            }
        }

        request.setAttribute("countryCode", geolocationResponse.countryCode());
        request.setAttribute("ipProvider", geolocationResponse.isp());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (long) request.getAttribute("timestamp");
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;

        logger.info("Persisting log record for request: " + request.getAttribute("uuid"));
        LogRecord logRecord = new LogRecord();
        logRecord.setUuid(String.valueOf(request.getAttribute("uuid")));
        logRecord.setTimestamp(startTime);
        logRecord.setTimeElapsed(timeElapsed);
        logRecord.setRequestUri(request.getRequestURI());
        logRecord.setHttpResponseCode(response.getStatus());
        logRecord.setRequestCountryCode(String.valueOf(request.getAttribute("countryCode")));
        logRecord.setRequestIpAddress(String.valueOf(request.getAttribute("ipAddress")));
        logRecord.setRequestIpProvider(String.valueOf(request.getAttribute("ipProvider")));
        logRecordRepository.save(logRecord);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null) {
            String[] ips = xForwardedForHeader.split(",");
            if (ips.length > 0) {
                return ips[0].trim();
            }
        }
        return request.getRemoteAddr();
    }
}
