package com.tobsec.context;

import org.springframework.context.annotation.Import;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@Import({LoggerConfig.class})
@ImportResource("classpath:security.xml")
public class SecurityConfig {}