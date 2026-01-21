package com.actora.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Utility class for accessing security context information.
 * Provides convenient methods to get current authenticated user.
 */
public final class SecurityUtils {

    private SecurityUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Get the current authenticated user principal.
     */
    public static Optional<UserPrincipal> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return Optional.of((UserPrincipal) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    /**
     * Get the current authenticated user's ID.
     */
    public static Optional<Long> getCurrentUserId() {
        return getCurrentUser().map(UserPrincipal::getUserId);
    }

    /**
     * Get the current authenticated username.
     */
    public static Optional<String> getCurrentUsername() {
        return getCurrentUser().map(UserPrincipal::getUsername);
    }

    /**
     * Check if current user has a specific role.
     */
    public static boolean hasRole(String role) {
        return getCurrentUser().map(user -> user.hasRole(role)).orElse(false);
    }

    /**
     * Check if current user is an admin.
     */
    public static boolean isAdmin() {
        return getCurrentUser().map(UserPrincipal::isAdmin).orElse(false);
    }

    /**
     * Check if current user is a manager.
     */
    public static boolean isManager() {
        return getCurrentUser().map(UserPrincipal::isManager).orElse(false);
    }

    /**
     * Check if current user is authenticated.
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserPrincipal;
    }
}
