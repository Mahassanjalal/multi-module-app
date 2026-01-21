package com.actora.authservice.entity;

/**
 * Enum representing user roles in the system.
 */
public enum Role {
    ROLE_USER,      // Basic user - can view own data, create orders
    ROLE_MANAGER,   // Manager - can manage orders, view all users
    ROLE_ADMIN      // Admin - full access to everything
}
