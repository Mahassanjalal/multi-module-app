package com.actora.common.constants;

/**
 * Application-wide constants.
 */
public final class AppConstants {

    private AppConstants() {
        // Prevent instantiation
    }

    // Pagination defaults
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    public static final int MAX_PAGE_SIZE = 100;

    // Header names
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    public static final String CLIENT_ID_HEADER = "X-Client-ID";

    // Date/Time formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATETIME_WITH_ZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
}
