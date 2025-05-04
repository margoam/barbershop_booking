package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.*;
import com.teachmeskills.tms_booking_project.model.dto.BookingRequest;
import com.teachmeskills.tms_booking_project.model.dto.BookingResponse;
import com.teachmeskills.tms_booking_project.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BarberScheduleRepository barberScheduleRepository;
    private final BarberRepository barberRepository;
    private final ServiceRepository serviceRepository;

    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public Booking getById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    @Transactional
    public Booking createBooking(BookingRequest bookingRequest) {
        User user = userRepository.findById(bookingRequest.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Barber barber = barberRepository.findById(bookingRequest.barberId())
                .orElseThrow(() -> new IllegalArgumentException("Barber not found"));
        BarberSrv srv = serviceRepository.findById(bookingRequest.serviceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        BarberSchedule schedule = barberScheduleRepository.findByBarberAndAvailableTrueAndBookedFalseAndStartTimeAfter(
                        barber, bookingRequest.appointmentTime())
                .stream()
                .filter(s -> s.getStartTime().equals(bookingRequest.appointmentTime()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Selected time is unavailable"));

        Booking booking = Booking.builder()
                .user(user)
                .barber(barber)
                .service(srv)
                .appointmentTime(bookingRequest.appointmentTime())
                .pricePaid(bookingRequest.pricePaid())
                .status(String.valueOf(Status.CONFIRMED))
                .createdAt(LocalDateTime.now())
                .build();

        Booking saved = bookingRepository.save(booking);

        schedule.setBooked(true);
        barberScheduleRepository.save(schedule);

        return saved;
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

    public boolean delete(Long id) {
        bookingRepository.deleteById(id);
        return true;
    }

    public BookingResponse mapToResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUser().getId(),
                booking.getUser().getFullName(),
                booking.getBarber().getId(),
                booking.getBarber().getFullName(),
                booking.getService().getId(),
                booking.getService().getName(),
                booking.getPricePaid(),
                booking.getStatus(),
                booking.getAppointmentTime(),
                booking.getCreatedAt()
        );
    }
}

