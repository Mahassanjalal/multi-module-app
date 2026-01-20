package com.actora.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends BusinessException {

    private static final String ERROR_CODE = "VALIDATION_ERROR";

    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ERROR_CODE);
    }
}
