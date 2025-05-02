package com.teachmeskills.tms_booking_project.model.dto;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        String role,
        boolean isSubscribed
) {
}
