package com.actora.orderservice.client;

import com.actora.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for communicating with User Service.
 */
@FeignClient(name = "user-service", fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {

    @GetMapping("/users/{id}")
    ApiResponse<UserResponse> getUserById(@PathVariable("id") Long id);

    @GetMapping("/users/exists/{id}")
    ApiResponse<Boolean> existsById(@PathVariable("id") Long id);

    /**
     * DTO for user data received from User Service.
     * This should match the UserResponse from user-service.
     */
    record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String fullName,
        String email,
        String phone,
        String address,
        String status,
        String createdAt,
        String updatedAt
    ) {}
}
