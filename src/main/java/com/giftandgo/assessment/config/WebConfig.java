package com.giftandgo.assessment.config;

import com.giftandgo.assessment.component.IpCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final IpCheckInterceptor ipCheckInterceptor;

    public WebConfig(IpCheckInterceptor ipCheckInterceptor) {
        this.ipCheckInterceptor = ipCheckInterceptor;
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipCheckInterceptor)
                .addPathPatterns("/api/**");
    }
}
