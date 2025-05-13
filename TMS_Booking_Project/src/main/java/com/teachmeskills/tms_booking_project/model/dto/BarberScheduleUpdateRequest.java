package com.teachmeskills.tms_booking_project.model.dto;

import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDateTime;

public record BarberScheduleUpdateRequest (

        @FutureOrPresent
        LocalDateTime startTime,
        @FutureOrPresent
        LocalDateTime endTime,

        Boolean isAvailable,

        Boolean isBooked
) {
}
