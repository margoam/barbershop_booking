package com.teachmeskills.tms_booking_project.repository;

import com.teachmeskills.tms_booking_project.model.Booking;
import com.teachmeskills.tms_booking_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
}
