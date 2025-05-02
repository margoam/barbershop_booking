package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.Booking;
import com.teachmeskills.tms_booking_project.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        return bookingService.getAll();
    }

    @PostMapping("/bookings")
    public Booking createBooking(@RequestBody @Valid Booking booking) {
        return bookingService.create(booking);
    }

    @PutMapping("/bookings/{id}")
    public Booking updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        return bookingService.update(id, booking);
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
