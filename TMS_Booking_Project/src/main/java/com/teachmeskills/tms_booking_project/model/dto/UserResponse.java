package com.teachmeskills.tms_booking_project.model.dto;

import com.teachmeskills.tms_booking_project.model.Role;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        Role role,
        boolean isSubscribed
) {
}
