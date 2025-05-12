package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.User;
import com.teachmeskills.tms_booking_project.model.dto.BookingRequest;
import com.teachmeskills.tms_booking_project.model.dto.BookingResponse;
import com.teachmeskills.tms_booking_project.model.dto.BookingUpdateRequest;
import com.teachmeskills.tms_booking_project.security.SecurityUtils;
import com.teachmeskills.tms_booking_project.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Validated
@Tag(name = "Booking Management", description = "API for booking operations")
public class BookingController {

    private final BookingService bookingService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "Get all bookings")
    @ApiResponse(responseCode = "200", description = "List of bookings found")
    @ApiResponse(responseCode = "404", description = "No bookings found")
    @GetMapping("/all")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.getAll().stream()
                .map(bookingService::mapToResponse)
                .toList();
        return bookings.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(bookings);
    }

    @Operation(summary = "Get booking by ID")
    @ApiResponse(responseCode = "200", description = "Booking found")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(
            @Parameter(description = "ID of the booking to retrieve")
            @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.mapToResponse(bookingService.getById(id)));
    }

    @Operation(summary = "Create new booking")
    @ApiResponse(responseCode = "201", description = "Booking created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "User, barber or service not found")
    @ApiResponse(responseCode = "409", description = "Time slot not available")
    @PostMapping("/create")
    public ResponseEntity<BookingResponse> createBooking(
            @RequestBody @Valid BookingRequest request) {

        BookingResponse response = bookingService.mapToResponse(bookingService.createBooking(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update booking")
    @ApiResponse(responseCode = "200", description = "Booking updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    @ApiResponse(responseCode = "409", description = "Cannot modify declined booking or time slot not available")
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(
            @Parameter(description = "ID of the booking to update")
            @PathVariable Long id,
            @RequestBody @Valid BookingUpdateRequest request) {

        User currentUser = securityUtils.getCurrentUser();

        if (!securityUtils.isCurrentUserAdmin() && !bookingService.isOwner(id, currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(bookingService.mapToResponse(bookingService.update(id, request)));
    }

    @Operation(summary = "Delete booking")
    @ApiResponse(responseCode = "204", description = "Booking deleted successfully")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBooking(
            @Parameter(description = "ID of the booking to delete")
            @PathVariable Long id) {
        bookingService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}