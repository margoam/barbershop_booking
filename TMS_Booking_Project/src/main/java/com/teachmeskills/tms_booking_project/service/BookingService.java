package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.Booking;
import com.teachmeskills.tms_booking_project.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public Booking getById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking update(Long id, Booking updated) {
        Booking existing = getById(id);
        existing.setAppointmentTime(updated.getAppointmentTime());
        existing.setPricePaid(updated.getPricePaid());
        existing.setCreatedAt(updated.getCreatedAt());
        existing.setStatus(updated.getStatus());
        existing.setUser(updated.getUser());
        existing.setBarber(updated.getBarber());
        existing.setService(updated.getService());
        return bookingRepository.save(existing);
    }

    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }
}

