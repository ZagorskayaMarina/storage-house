package com.incorparation.configuration;

import com.incorparation.interceptors.CacheControlInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorAppConfig implements WebMvcConfigurer {
    CacheControlInterceptor cacheControlInterceptor;

    @Autowired
    public InterceptorAppConfig(CacheControlInterceptor cacheControlInterceptor) {
        this.cacheControlInterceptor = cacheControlInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cacheControlInterceptor);
    }
}
