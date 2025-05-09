package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.Booking;
import com.teachmeskills.tms_booking_project.model.dto.BookingRequest;
import com.teachmeskills.tms_booking_project.model.dto.BookingResponse;
import com.teachmeskills.tms_booking_project.model.dto.BookingUpdateRequest;
import com.teachmeskills.tms_booking_project.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/all")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> bookingResponses = bookingService.getAll().stream()
                .map(bookingService::mapToResponse)
                .toList();
        if (bookingResponses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookingResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = Optional.ofNullable(bookingService.getById(id));
        if (booking.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(bookingService.mapToResponse(booking.get()));
    }

    @PostMapping("/create")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid BookingRequest request) {
        Optional<Booking> booking = Optional.ofNullable(bookingService.createBooking(request));
        if (booking.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(bookingService.mapToResponse(booking.get()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long id, @RequestBody @Valid BookingUpdateRequest request) {
        Optional<Booking> booking = Optional.ofNullable(bookingService.update(id, request));
        if (booking.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(bookingService.mapToResponse(booking.get()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBooking(@PathVariable Long id) {
        boolean result = bookingService.delete(id);
        if (!result) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
