package com.actora.common.security;

/**
 * Constants for security-related headers and values.
 * Centralized to ensure consistency across all services.
 */
public final class SecurityConstants {

    private SecurityConstants() {
        // Utility class - prevent instantiation
    }

    // HTTP Header names used by API Gateway
    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USER_NAME = "X-User-Name";
    public static final String HEADER_USER_ROLES = "X-User-Roles";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // Role names
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_MANAGER = "ROLE_MANAGER";
    public static final String ROLE_USER = "ROLE_USER";

    // Common public endpoints patterns
    public static final String[] PUBLIC_ENDPOINTS = {
        "/actuator/**",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/h2-console/**"
    };
}
