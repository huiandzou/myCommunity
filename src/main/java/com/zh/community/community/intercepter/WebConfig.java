package com.zh.community.community.intercepter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * created by ${host}
 */
//@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    SessionIntercepter sessionIntercepter;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionIntercepter).addPathPatterns("/**");
    }
}
