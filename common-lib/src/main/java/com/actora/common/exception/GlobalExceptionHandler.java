package com.actora.common.exception;

import com.actora.common.dto.ApiResponse;
import com.actora.common.dto.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for consistent error responses across all microservices.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        log.error("Business exception: {} - Path: {}", ex.getMessage(), request.getRequestURI());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(ex.getStatus())
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("Validation exception: {} - Path: {}", ex.getMessage(), request.getRequestURI());

        List<ErrorDetails.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> ErrorDetails.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .rejectedValue(error.getRejectedValue())
                        .build())
                .collect(Collectors.toList());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("VALIDATION_ERROR")
                .message("Validation failed")
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation failed", errorDetails));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Void>> handleBindException(
            BindException ex, HttpServletRequest request) {
        log.error("Bind exception: {} - Path: {}", ex.getMessage(), request.getRequestURI());

        List<ErrorDetails.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> ErrorDetails.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .rejectedValue(error.getRejectedValue())
                        .build())
                .collect(Collectors.toList());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("BINDING_ERROR")
                .message("Request binding failed")
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Request binding failed", errorDetails));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        log.error("Constraint violation: {} - Path: {}", ex.getMessage(), request.getRequestURI());

        List<ErrorDetails.FieldError> fieldErrors = ex.getConstraintViolations().stream()
                .map(violation -> ErrorDetails.FieldError.builder()
                        .field(violation.getPropertyPath().toString())
                        .message(violation.getMessage())
                        .rejectedValue(violation.getInvalidValue())
                        .build())
                .collect(Collectors.toList());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("CONSTRAINT_VIOLATION")
                .message("Constraint violation")
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Constraint violation", errorDetails));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.error("Type mismatch: {} - Path: {}", ex.getMessage(), request.getRequestURI());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("TYPE_MISMATCH")
                .message(String.format("Invalid value '%s' for parameter '%s'", ex.getValue(), ex.getName()))
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Invalid parameter type", errorDetails));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex, HttpServletRequest request) {
        log.error("Unexpected error: {} - Path: {}", ex.getMessage(), request.getRequestURI(), ex);

        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("INTERNAL_ERROR")
                .message("An unexpected error occurred")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error", errorDetails));
    }
}
