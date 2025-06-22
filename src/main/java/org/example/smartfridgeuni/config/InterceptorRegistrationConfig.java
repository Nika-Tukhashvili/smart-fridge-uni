package org.example.smartfridgeuni.config;

import lombok.RequiredArgsConstructor;
import org.example.smartfridgeuni.interceptor.AppInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorRegistrationConfig implements WebMvcConfigurer {

    private final AppInterceptor appInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(appInterceptor);
    }

}
