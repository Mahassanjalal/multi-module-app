package com.actora.userservice.dto;

import com.actora.userservice.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for updating an existing user")
public class UpdateUserRequest {

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Email(message = "Invalid email format")
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    @Schema(description = "User's phone number", example = "+1-555-123-4567")
    private String phone;

    @Size(max = 200, message = "Address cannot exceed 200 characters")
    @Schema(description = "User's address", example = "123 Main St, City, Country")
    private String address;

    @Schema(description = "User's status", example = "ACTIVE")
    private User.UserStatus status;
}
