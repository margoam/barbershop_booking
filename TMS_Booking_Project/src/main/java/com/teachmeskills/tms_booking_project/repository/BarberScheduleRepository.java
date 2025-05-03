package com.teachmeskills.tms_booking_project.repository;

import com.teachmeskills.tms_booking_project.model.Barber;
import com.teachmeskills.tms_booking_project.model.BarberSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BarberScheduleRepository extends JpaRepository<BarberSchedule, Long> {
    List<BarberSchedule> findByBarberAndAvailableTrueAndBookedFalseAndStartTimeAfter(
            Barber barber, LocalDateTime time);
}
