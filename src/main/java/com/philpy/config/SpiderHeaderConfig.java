package com.philpy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spider")
public class SpiderHeaderConfig {
    private Map<String, String> header = new HashMap<>();

    @Bean
    public Map<String, String> getHeader() {
        return header;
    }
}
