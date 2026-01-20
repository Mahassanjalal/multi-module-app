package com.actora.userservice.repository;

import com.actora.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity with custom query methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email address.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user with the given email exists.
     */
    boolean existsByEmail(String email);

    /**
     * Check if a user with the given email exists, excluding a specific user ID.
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Find all users by status.
     */
    List<User> findByStatus(User.UserStatus status);

    /**
     * Find all users by status with pagination.
     */
    Page<User> findByStatus(User.UserStatus status, Pageable pageable);

    /**
     * Search users by name (first name or last name) containing the search term.
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchByName(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Search users by multiple criteria.
     */
    @Query("SELECT u FROM User u WHERE " +
           "(:searchTerm IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND (:status IS NULL OR u.status = :status)")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm,
                          @Param("status") User.UserStatus status,
                          Pageable pageable);
}
