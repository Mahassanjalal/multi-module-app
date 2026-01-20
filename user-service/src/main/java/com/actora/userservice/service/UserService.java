package com.actora.userservice.service;

import com.actora.userservice.dto.CreateUserRequest;
import com.actora.userservice.dto.UpdateUserRequest;
import com.actora.userservice.dto.UserResponse;
import com.actora.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for User operations.
 */
public interface UserService {

    /**
     * Create a new user.
     */
    UserResponse createUser(CreateUserRequest request);

    /**
     * Get a user by ID.
     */
    UserResponse getUserById(Long id);

    /**
     * Get a user by email.
     */
    UserResponse getUserByEmail(String email);

    /**
     * Get all users with pagination.
     */
    Page<UserResponse> getAllUsers(Pageable pageable);

    /**
     * Get all users by status.
     */
    List<UserResponse> getUsersByStatus(User.UserStatus status);

    /**
     * Search users by name or email.
     */
    Page<UserResponse> searchUsers(String searchTerm, User.UserStatus status, Pageable pageable);

    /**
     * Update an existing user.
     */
    UserResponse updateUser(Long id, UpdateUserRequest request);

    /**
     * Delete a user by ID.
     */
    void deleteUser(Long id);

    /**
     * Check if a user exists by ID.
     */
    boolean existsById(Long id);

    /**
     * Check if a user exists by email.
     */
    boolean existsByEmail(String email);
}
