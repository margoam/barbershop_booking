package com.teachmeskills.tms_booking_project.model.dto;

import com.teachmeskills.tms_booking_project.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

        String fullName,

        @Email(message = "Email should be valid")
        String email,

        @Size(min = 12, max = 16, message = "Password must be between 12 and 16 characters")
        String password,

        @Schema(description = "User role", allowableValues = {"USER", "ADMIN"})
        @Enumerated(EnumType.STRING)
        Role role,

        Boolean isSubscribed
) {
}