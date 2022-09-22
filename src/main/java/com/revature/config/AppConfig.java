package com.revature.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ComponentScan("com.revature")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public ObjectMapper jsonMapper() {
        return new ObjectMapper();
    }
    
}
