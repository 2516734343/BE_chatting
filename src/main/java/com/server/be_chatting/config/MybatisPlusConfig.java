package com.server.be_chatting.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

import lombok.extern.slf4j.Slf4j;

@ConditionalOnClass(value = {PaginationInterceptor.class})
@Configuration
@MapperScan("com.server.be_chatting.dao")
@Slf4j
public class MybatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        log.info("MybatisPlusConfig.paginationInterceptor init");
        return new PaginationInterceptor();
    }
}