package com.teachmeskills.tms_booking_project.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 * @param fullName
 * @param email
 * @param password
 * @param role
 * @param isSubscribed
 * For admins usage only!
 */
public record UserCreateRequest(
        @NotBlank(message = "Full name is required")
        String fullName,

        @Email(message = "Email should be valid")
        String email,

        @Size(min = 12, max = 16, message = "Password must be between 12 and 16 characters")
        String password,

        @NotBlank(message = "Role is required")
        String role,

        @NotNull
        boolean isSubscribed
) {
}

