package com.tobsec.context;

import com.tobsec.common.*;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@ImportResource({"classpath:applicationContext.xml"})
public class AppConfig {

    @Bean(name="connBeanFactory")
    public ConnectionBeanFactory connBeanFactory() {
        return new ConnectionBeanFactory();
    }

}