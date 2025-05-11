package com.teachmeskills.tms_booking_project.model.dto;

import com.teachmeskills.tms_booking_project.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 12, max = 16, message = "Password must be between 12 and 16 characters")
        String password,

        @Schema(description = "User role", allowableValues = {"USER", "ADMIN"})
        @NotNull
        @Enumerated(EnumType.STRING)
        Role role,

        @NotNull
        boolean isSubscribed
) {
}

