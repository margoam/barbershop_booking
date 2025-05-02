package com.teachmeskills.tms_booking_project.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(
        @NotBlank(message = "Full name is required")
        String fullName,

        @Email(message = "Email should be valid")
        String email,

        @Size(min = 12, max = 16, message = "Password must be between 12 and 16 characters")
        String password
) {
}