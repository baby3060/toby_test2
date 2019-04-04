package com.tobsec.context;

import com.tobsec.common.*;

import org.springframework.context.annotation.Import;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@Import(RowMapperConfig.class)
@ImportResource({"classpath:applicationContext.xml"})
public class AppConfig {

    @Bean(name="connBeanFactory")
    public ConnectionBeanFactory connBeanFactory() {
        return new ConnectionBeanFactory();
    }
}