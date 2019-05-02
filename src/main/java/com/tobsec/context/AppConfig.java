package com.tobsec.context;

import com.tobsec.common.*;

import org.springframework.context.annotation.Import;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@Import({RowMapperConfig.class, SecurityConfig.class})
@ImportResource({"classpath:applicationContext.xml", "classpath:applicationContext1.xml", "classpath:applicationContext2.xml", "classpath:applicationContext3.xml"})
public class AppConfig {

    @Bean(name="connBeanFactory")
    public ConnectionBeanFactory connBeanFactory() {
        return new ConnectionBeanFactory();
    }
}