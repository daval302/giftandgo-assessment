package com.giftandgo.assessment.config;

import com.giftandgo.assessment.component.RequestLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BeansConfig {

    @Bean
    public RestClient restClient(RequestLoggingInterceptor requestLoggingInterceptor) {
        return RestClient.builder()
                .requestInterceptor(requestLoggingInterceptor)
                .baseUrl("http://ip-api.com")
                .build();
    }

}
