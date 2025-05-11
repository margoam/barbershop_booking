package com.teachmeskills.tms_booking_project.model.dto;

import com.teachmeskills.tms_booking_project.model.Status;
import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public record BookingUpdateRequest(
        @Future
        LocalDateTime appointmentTime,
        Status status
) {}
