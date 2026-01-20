package com.actora.orderservice.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign client configuration for logging and other settings.
 */
@Configuration
public class FeignConfig {

    /**
     * Configure Feign logging level.
     * FULL - Log the headers, body, and metadata for both requests and responses.
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
