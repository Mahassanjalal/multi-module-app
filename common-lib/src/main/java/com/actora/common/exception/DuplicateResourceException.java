package com.actora.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a duplicate resource is detected.
 */
public class DuplicateResourceException extends BusinessException {

    private static final String ERROR_CODE = "DUPLICATE_RESOURCE";

    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT, ERROR_CODE);
    }

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue),
                HttpStatus.CONFLICT, ERROR_CODE);
    }
}
