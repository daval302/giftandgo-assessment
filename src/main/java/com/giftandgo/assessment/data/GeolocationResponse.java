package com.giftandgo.assessment.data;

public record GeolocationResponse(
        String status,
        String country,
        String countryCode,
        String region,
        String regionName,
        String city,
        String zip,
        double lat,
        double lon,
        String timezone,
        String isp,
        String org,
        String as,
        String query

) {
}
