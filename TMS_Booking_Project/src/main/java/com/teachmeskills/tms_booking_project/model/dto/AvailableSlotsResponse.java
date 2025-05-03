package com.teachmeskills.tms_booking_project.model.dto;

import java.time.LocalDateTime;

public record AvailableSlotsResponse(
        Long scheduleId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
