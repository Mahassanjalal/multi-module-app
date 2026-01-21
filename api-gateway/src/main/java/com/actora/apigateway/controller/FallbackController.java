package com.actora.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Fallback controller for circuit breaker fallback routes.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/users")
    public Mono<ResponseEntity<Map<String, Object>>> userServiceFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(buildFallbackResponse("User Service is currently unavailable. Please try again later.")));
    }

    @GetMapping("/orders")
    public Mono<ResponseEntity<Map<String, Object>>> orderServiceFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(buildFallbackResponse("Order Service is currently unavailable. Please try again later.")));
    }

    @GetMapping("auth")
    public Mono<ResponseEntity<Map<String, Object>>> authServiceFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(buildFallbackResponse("Authentication Service is currently unavailable. Please try again later.")));
    }

    @GetMapping("/default")
    public Mono<ResponseEntity<Map<String, Object>>> defaultFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(buildFallbackResponse("Service is currently unavailable. Please try again later.")));
    }

    private Map<String, Object> buildFallbackResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now().toString());

        Map<String, Object> error = new HashMap<>();
        error.put("code", "SERVICE_UNAVAILABLE");
        error.put("message", message);
        response.put("error", error);

        return response;
    }
}
