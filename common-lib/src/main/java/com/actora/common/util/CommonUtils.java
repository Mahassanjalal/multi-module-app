package com.actora.common.util;

import java.util.UUID;

/**
 * Utility class for common operations.
 */
public final class CommonUtils {

    private CommonUtils() {
        // Prevent instantiation
    }

    /**
     * Generate a unique correlation ID for request tracing.
     */
    public static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Check if a string is null or empty.
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check if a string is not null and not empty.
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Safe null-coalescing for strings.
     */
    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    /**
     * Mask sensitive data for logging (shows first and last 2 chars).
     */
    public static String maskSensitiveData(String data) {
        if (isEmpty(data) || data.length() <= 4) {
            return "****";
        }
        return data.substring(0, 2) + "****" + data.substring(data.length() - 2);
    }
}
