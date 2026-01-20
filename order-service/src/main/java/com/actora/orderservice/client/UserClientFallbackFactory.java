package com.actora.orderservice.client;

import com.actora.common.dto.ApiResponse;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * Fallback factory for UserClient providing detailed error logging
 * and graceful degradation when User Service is unavailable.
 */
@Component
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

    private static final Logger log = LoggerFactory.getLogger(UserClientFallbackFactory.class);

    @Override
    public UserClient create(Throwable cause) {
        // Log the actual cause of the fallback
        if (cause instanceof FeignException) {
            log.error("Feign exception calling User Service: {} - {}",
                    cause.getClass().getSimpleName(), cause.getMessage());
        } else {
            log.error("Error calling User Service: {} - {}",
                    cause.getClass().getSimpleName(), cause.getMessage());
        }

        return new UserClient() {
            @Override
            public ApiResponse<UserResponse> getUserById(Long id) {
                log.warn("Fallback: Returning default user for ID: {} due to: {}", id, cause.getMessage());
                UserResponse fallbackUser = new UserResponse(
                        id,
                        "Unknown",
                        "User",
                        "Unknown User",
                        "unknown@example.com",
                        null,
                        null,
                        "UNKNOWN",
                        null,
                        null
                );
                return ApiResponse.success(fallbackUser, "Fallback response - User Service unavailable");
            }

            @Override
            public ApiResponse<Boolean> existsById(Long id) {
                log.warn("Fallback: Assuming user exists for ID: {} due to: {}", id, cause.getMessage());
                // In fallback, we assume user exists to not block order creation
                return ApiResponse.success(true, "Fallback response - User Service unavailable");
            }
        };
    }
}
