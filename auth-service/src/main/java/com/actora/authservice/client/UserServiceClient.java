package com.actora.authservice.client;

import com.actora.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client for communicating with User Service.
 * Auth service uses this to create user profiles during registration.
 */
@FeignClient(name = "user-service", fallbackFactory = UserServiceClientFallbackFactory.class)
public interface UserServiceClient {

    @PostMapping("/users")
    ApiResponse<UserDto> createUser(@RequestBody CreateUserRequest request);

    @GetMapping("/users/{id}")
    ApiResponse<UserDto> getUserById(@PathVariable("id") Long id);

    @GetMapping("/users/email/{email}")
    ApiResponse<UserDto> getUserByEmail(@PathVariable("email") String email);

    @GetMapping("/users/exists/{id}")
    ApiResponse<Boolean> existsById(@PathVariable("id") Long id);

    /**
     * Request DTO for creating a user in user-service
     */
    record CreateUserRequest(
        String firstName,
        String lastName,
        String email,
        String phone,
        String address
    ) {}

    /**
     * Response DTO from user-service
     */
    record UserDto(
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
