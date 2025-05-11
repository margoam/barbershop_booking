package com.teachmeskills.tms_booking_project.model.dto;

import com.teachmeskills.tms_booking_project.model.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long userId,
        String userName,
        Long barberId,
        String barberName,
        Long serviceId,
        String serviceName,
        BigDecimal pricePaid,
        Status status,
        LocalDateTime appointmentTime,
        LocalDateTime createdAt
) {
}

