package com.actora.userservice.service.impl;

import com.actora.common.exception.DuplicateResourceException;
import com.actora.common.exception.ResourceNotFoundException;
import com.actora.userservice.dto.CreateUserRequest;
import com.actora.userservice.dto.UpdateUserRequest;
import com.actora.userservice.dto.UserResponse;
import com.actora.userservice.entity.User;
import com.actora.userservice.mapper.UserMapper;
import com.actora.userservice.repository.UserRepository;
import com.actora.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of UserService with business logic for user management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating new user with email: {}", request.getEmail());

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        User user = userMapper.toEntity(request);
        if (user.getStatus() == null) {
            user.setStatus(User.UserStatus.ACTIVE);
        }

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());

        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(Long id) {
        log.debug("Fetching user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        log.debug("Fetching user with email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return userMapper.toResponse(user);
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users with pagination: {}", pageable);

        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }

    @Override
    public List<UserResponse> getUsersByStatus(User.UserStatus status) {
        log.debug("Fetching users with status: {}", status);

        return userMapper.toResponseList(userRepository.findByStatus(status));
    }

    @Override
    public Page<UserResponse> searchUsers(String searchTerm, User.UserStatus status, Pageable pageable) {
        log.debug("Searching users with term: '{}', status: {}", searchTerm, status);

        return userRepository.searchUsers(searchTerm, status, pageable)
                .map(userMapper::toResponse);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Check if email is being changed and if it's already taken
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
                throw new DuplicateResourceException("User", "email", request.getEmail());
            }
        }

        userMapper.updateEntityFromDto(request, user);
        User updatedUser = userRepository.save(user);

        log.info("User updated successfully with ID: {}", updatedUser.getId());
        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
