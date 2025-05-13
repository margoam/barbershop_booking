package com.teachmeskills.tms_booking_project.repository;

import com.teachmeskills.tms_booking_project.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingRepository extends JpaRepository<Booking, Long> {
}
