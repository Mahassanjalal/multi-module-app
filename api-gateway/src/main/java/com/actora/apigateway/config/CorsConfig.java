package com.actora.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * CORS Configuration for API Gateway.
 * Enables cross-origin requests for Swagger UI and frontend applications.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Allow all origins (for development) - restrict in production
        corsConfig.setAllowedOriginPatterns(List.of("*"));

        // Allow all common HTTP methods
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));

        // Allow all headers
        corsConfig.setAllowedHeaders(List.of("*"));

        // Expose headers that clients can access
        corsConfig.setExposedHeaders(List.of(
            "Authorization",
            "Content-Type",
            "X-User-Id",
            "X-User-Name",
            "X-User-Roles",
            "X-Correlation-ID"
        ));

        // Allow credentials (cookies, authorization headers)
        corsConfig.setAllowCredentials(true);

        // Cache preflight response for 1 hour
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
