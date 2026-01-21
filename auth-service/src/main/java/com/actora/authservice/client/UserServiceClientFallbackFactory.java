package com.actora.authservice.client;

import com.actora.common.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * Fallback factory for UserServiceClient when User Service is unavailable.
 */
@Component
public class UserServiceClientFallbackFactory implements FallbackFactory<UserServiceClient> {

    private static final Logger log = LoggerFactory.getLogger(UserServiceClientFallbackFactory.class);

    @Override
    public UserServiceClient create(Throwable cause) {
        log.error("User Service fallback triggered: {}", cause.getMessage());

        return new UserServiceClient() {
            @Override
            public ApiResponse<UserDto> createUser(CreateUserRequest request) {
                log.warn("Fallback: Cannot create user in user-service. Error: {}", cause.getMessage());
                return ApiResponse.error("User Service unavailable - cannot create user profile");
            }

            @Override
            public ApiResponse<UserDto> getUserById(Long id) {
                log.warn("Fallback: Cannot get user from user-service. ID: {}", id);
                return ApiResponse.error("User Service unavailable");
            }

            @Override
            public ApiResponse<UserDto> getUserByEmail(String email) {
                log.warn("Fallback: Cannot get user by email from user-service. Email: {}", email);
                return ApiResponse.error("User Service unavailable");
            }

            @Override
            public ApiResponse<Boolean> existsById(Long id) {
                log.warn("Fallback: Cannot check user existence. Assuming exists for ID: {}", id);
                return ApiResponse.success(true, "Fallback - assuming user exists");
            }
        };
    }
}
