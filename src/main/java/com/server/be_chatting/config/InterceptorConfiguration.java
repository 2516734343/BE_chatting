package com.server.be_chatting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.server.be_chatting.auth.InternalApiAuthInterceptor;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Autowired
    private InternalApiAuthInterceptor internalApiAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(internalApiAuthInterceptor)
                .addPathPatterns("/api/be/chatting/**").excludePathPatterns("/api/be/chatting/common/**")
                .excludePathPatterns("/api/be/chatting/websocket/**");
        log.info("InterceptorConfiguration init");
    }
}
