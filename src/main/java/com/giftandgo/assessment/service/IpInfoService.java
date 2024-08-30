package com.giftandgo.assessment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giftandgo.assessment.data.GeolocationResponse;
import com.giftandgo.assessment.error.exception.InvalidIpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;
import java.util.Optional;

@Service
public class IpInfoService {
    private final Logger logger = LoggerFactory.getLogger(IpInfoService.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public IpInfoService(RestClient restClient) {
        this.restClient = restClient;
    }

    public GeolocationResponse getGeolocationInfo(String ipAddress) {
        Map<String, String> geolocationResponseMap = restClient.get()
                .uri("/json/" + ipAddress)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        (clientRequest, clientResponse) -> {
                            logger.error("Failed to get geolocation data for IP: " + ipAddress);
                            throw new RestClientException("Failed to get geolocation data for IP: " + ipAddress);
                        })
                .body(new ParameterizedTypeReference<Map<String, String>>() {
                });

        Optional.ofNullable(geolocationResponseMap)
                .map(map -> map.get("status"))
                .filter("fail"::equals)
                .ifPresent(status -> {
                    if (geolocationResponseMap.containsKey("message")) {
                        if ("private range".equals(geolocationResponseMap.get("message")))
                            throw new InvalidIpException("Private IP address detected (AWS,GCP,Azure,etc..): " + ipAddress, ipAddress);
                    }
                    throw new RestClientException("Failed to get geolocation data for IP: " + ipAddress);
                });
        return objectMapper.convertValue(geolocationResponseMap, GeolocationResponse.class);
    }
}
