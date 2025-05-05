package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.*;
import com.teachmeskills.tms_booking_project.model.dto.BookingRequest;
import com.teachmeskills.tms_booking_project.model.dto.BookingResponse;
import com.teachmeskills.tms_booking_project.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

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
        BarberSrv service = serviceRepository.findById(bookingRequest.serviceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        BarberSchedule schedule = barberScheduleRepository.findByBarberAndAvailableTrueAndBookedFalseAndStartTimeAfter(
                        barber, bookingRequest.appointmentTime())
                .stream()
                .filter(s -> s.getStartTime().equals(bookingRequest.appointmentTime()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Selected time is unavailable"));

        BigDecimal finalPrice = service.getPrice();
        if (user.isSubscribed()) {
            finalPrice = finalPrice.multiply(BigDecimal.valueOf(0.9));
            finalPrice = finalPrice.setScale(2, RoundingMode.HALF_UP);
            logger.info("Applied 10% discount for subscribed user {}", user.getEmail());
        }

        if (bookingRequest.appointmentTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment time must be in the future");
        }

        if (!service.isActive()) {
            throw new IllegalArgumentException("Service is not available");
        }

        Booking booking = Booking.builder()
                .user(user)
                .barber(barber)
                .service(service)
                .appointmentTime(bookingRequest.appointmentTime())
                .pricePaid(finalPrice)
                .status(Status.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .build();

        Booking saved = bookingRepository.save(booking);

        schedule.setBooked(true);
        barberScheduleRepository.save(schedule);

        logger.info("Created booking with id: {} for user {}. Final price: {}",
                saved.getId(), user.getEmail(), finalPrice);

        return saved;
    }

    @Transactional
    public Booking update(Long id, Booking updated) {
        Booking existing = getById(id);
        existing.setAppointmentTime(updated.getAppointmentTime());
        existing.setPricePaid(updated.getPricePaid());
        existing.setCreatedAt(updated.getCreatedAt());
        existing.setStatus(updated.getStatus());
        existing.setUser(updated.getUser());
        existing.setBarber(updated.getBarber());
        existing.setService(updated.getService());
        logger.info("Updated booking with id: {}", existing.getId());
        return bookingRepository.save(existing);
    }

    public boolean delete(Long id) {
        bookingRepository.deleteById(id);
        logger.info("Deleted booking with id: {}", id);
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

