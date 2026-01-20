package com.actora.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a service call fails.
 */
public class ServiceException extends BusinessException {

    private static final String ERROR_CODE = "SERVICE_ERROR";

    public ServiceException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE, ERROR_CODE);
    }

    public ServiceException(String serviceName, String message) {
        super(String.format("Service '%s' error: %s", serviceName, message),
                HttpStatus.SERVICE_UNAVAILABLE, ERROR_CODE);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
