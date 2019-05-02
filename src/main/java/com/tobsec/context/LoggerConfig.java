package com.tobsec.context;

import org.springframework.context.annotation.Import;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:loggerContext.xml")
public class LoggerConfig {

}