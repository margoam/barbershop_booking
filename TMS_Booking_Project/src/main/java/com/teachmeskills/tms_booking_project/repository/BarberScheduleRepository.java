package com.teachmeskills.tms_booking_project.repository;

import com.teachmeskills.tms_booking_project.model.Barber;
import com.teachmeskills.tms_booking_project.model.BarberSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BarberScheduleRepository extends JpaRepository<BarberSchedule, Long> {
    List<BarberSchedule> findByBarberAndAvailableTrueAndBookedFalseAndStartTimeAfter(
            Barber barber, LocalDateTime time);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM BarberSchedule s " +
            "WHERE s.barber.id = :barberId " +
            "AND s.startTime < :endTime " +
            "AND s.endTime > :startTime")

    boolean existsByBarberIdAndTimeRange(
            @Param("barberId") Long barberId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
