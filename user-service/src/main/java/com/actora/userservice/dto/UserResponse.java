package com.actora.userservice.dto;

import com.actora.userservice.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user response data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "User response data")
public class UserResponse {

    @Schema(description = "User's unique identifier", example = "1")
    private Long id;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's full name", example = "John Doe")
    private String fullName;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "User's phone number", example = "+1-555-123-4567")
    private String phone;

    @Schema(description = "User's address", example = "123 Main St, City, Country")
    private String address;

    @Schema(description = "User's status", example = "ACTIVE")
    private User.UserStatus status;

    @Schema(description = "User creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "User last update timestamp")
    private LocalDateTime updatedAt;
}
