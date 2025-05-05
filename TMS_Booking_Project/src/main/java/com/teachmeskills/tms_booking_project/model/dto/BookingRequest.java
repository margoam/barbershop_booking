package com.teachmeskills.tms_booking_project.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookingRequest(
        @NotNull(message = "userId is required")
        Long userId,
        @NotNull(message = "barberId is required")
        Long barberId,
        @NotNull(message = "serviceId is required")
        Long serviceId,
        @Future
        LocalDateTime appointmentTime
) {
}
