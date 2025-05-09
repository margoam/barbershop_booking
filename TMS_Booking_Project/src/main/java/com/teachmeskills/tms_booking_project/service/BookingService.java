package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.*;
import com.teachmeskills.tms_booking_project.model.dto.BookingRequest;
import com.teachmeskills.tms_booking_project.model.dto.BookingResponse;
import com.teachmeskills.tms_booking_project.model.dto.BookingUpdateRequest;
import com.teachmeskills.tms_booking_project.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BarberScheduleRepository barberScheduleRepository;
    private final BarberRepository barberRepository;
    private final ServiceRepository serviceRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    public BookingService(UserRepository userRepository, BookingRepository bookingRepository, BarberScheduleRepository barberScheduleRepository, BarberRepository barberRepository, ServiceRepository serviceRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.barberScheduleRepository = barberScheduleRepository;
        this.barberRepository = barberRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public Booking getById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    @Transactional
    public Booking createBooking(BookingRequest bookingRequest) {
        validateBookingRequest(bookingRequest);

        User user = userRepository.findById(bookingRequest.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Barber barber = barberRepository.findById(bookingRequest.barberId())
                .orElseThrow(() -> new EntityNotFoundException("Barber not found"));
        BarberSrv service = serviceRepository.findById(bookingRequest.serviceId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));

        LocalDateTime appointmentTime = bookingRequest.appointmentTime();
        int durationMinutes = service.getDurationMinutes();
        LocalDateTime endTime = appointmentTime.plusMinutes(durationMinutes);

        BarberSchedule schedule = barberScheduleRepository
                .findAvailableSlotForBooking(barber, appointmentTime, endTime)
                .orElseThrow(() -> new IllegalStateException("Selected time is not available"));

        BigDecimal finalPrice = calculateFinalPrice(service.getPrice(), user.isSubscribed());

        Booking booking = createNewBooking(user, barber, service, appointmentTime, finalPrice);
        updateScheduleAfterBooking(schedule, appointmentTime, endTime);

        logger.info("Booking created: {} for user {} at {}, duration {} min, price: {}",
                booking.getId(), user.getEmail(), appointmentTime, durationMinutes, finalPrice);

        return booking;
    }

    @Transactional
    public Booking update(Long id, BookingUpdateRequest updateRequest) {
        Booking existing = getById(id);

        if (existing.getStatus() == Status.DECLINED) {
            throw new IllegalStateException("Cannot modify declined booking");
        }

        if (updateRequest.appointmentTime() != null
                && !updateRequest.appointmentTime().equals(existing.getAppointmentTime())) {
            updateBookingTime(existing, updateRequest.appointmentTime());
        }

        if (updateRequest.status() != null) {
            existing.setStatus(updateRequest.status());
        }

        logger.info("Updated booking with id: {}", existing.getId());
        return bookingRepository.save(existing);
    }

    @Transactional
    public boolean delete(Long id) {
        Booking booking = getById(id);

        LocalDateTime startTime = booking.getAppointmentTime();
        LocalDateTime endTime = startTime.plusMinutes(booking.getService().getDurationMinutes());

        freeUpOldTimeSlot(booking.getBarber(), startTime, endTime);

        bookingRepository.delete(booking);

        logger.info("Deleted booking with id: {} and freed time slot from {} to {}",
                id, startTime, endTime);
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

    private void validateBookingRequest(BookingRequest bookingRequest) {
        if (bookingRequest.appointmentTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment time must be in the future");
        }
    }

    private BigDecimal calculateFinalPrice(BigDecimal basePrice, boolean isSubscribed) {
        if (isSubscribed) {
            BigDecimal discountedPrice = basePrice.multiply(BigDecimal.valueOf(0.9));
            return discountedPrice.setScale(2, RoundingMode.HALF_UP);
        }
        return basePrice;
    }

    private Booking createNewBooking(User user, Barber barber, BarberSrv service,
                                     LocalDateTime time, BigDecimal price) {
        return bookingRepository.save(Booking.builder()
                .user(user)
                .barber(barber)
                .service(service)
                .appointmentTime(time)
                .pricePaid(price)
                .status(Status.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .build());
    }

    private void updateScheduleAfterBooking(BarberSchedule originalSlot,
                                            LocalDateTime bookingStart,
                                            LocalDateTime bookingEnd) {

        if (originalSlot.getStartTime().equals(bookingStart) &&
                originalSlot.getEndTime().equals(bookingEnd)) {
            originalSlot.setBooked(true);
            barberScheduleRepository.save(originalSlot);
            return;
        }

        if (originalSlot.getStartTime().equals(bookingStart)) {
            BarberSchedule remainingSlot = BarberSchedule.builder()
                    .barber(originalSlot.getBarber())
                    .startTime(bookingEnd)
                    .endTime(originalSlot.getEndTime())
                    .available(true)
                    .booked(false)
                    .build();
            barberScheduleRepository.save(remainingSlot);
            originalSlot.setEndTime(bookingEnd);
            originalSlot.setBooked(true);
            barberScheduleRepository.save(originalSlot);
            return;
        }

        if (originalSlot.getEndTime().equals(bookingEnd)) {
            BarberSchedule remainingSlot = BarberSchedule.builder()
                    .barber(originalSlot.getBarber())
                    .startTime(originalSlot.getStartTime())
                    .endTime(bookingStart)
                    .available(true)
                    .booked(false)
                    .build();
            barberScheduleRepository.save(remainingSlot);
            originalSlot.setStartTime(bookingStart);
            originalSlot.setBooked(true);
            barberScheduleRepository.save(originalSlot);
            return;
        }

        BarberSchedule slotBefore = BarberSchedule.builder()
                .barber(originalSlot.getBarber())
                .startTime(originalSlot.getStartTime())
                .endTime(bookingStart)
                .available(true)
                .booked(false)
                .build();

        BarberSchedule slotAfter = BarberSchedule.builder()
                .barber(originalSlot.getBarber())
                .startTime(bookingEnd)
                .endTime(originalSlot.getEndTime())
                .available(true)
                .booked(false)
                .build();

        barberScheduleRepository.save(slotBefore);
        barberScheduleRepository.save(slotAfter);
        originalSlot.setStartTime(bookingStart);
        originalSlot.setEndTime(bookingEnd);
        originalSlot.setBooked(true);
        barberScheduleRepository.save(originalSlot);
    }

    private void updateBookingTime(Booking booking, LocalDateTime newTime) {

        if (newTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("New appointment time must be in the future");
        }

        LocalDateTime oldStart = booking.getAppointmentTime();
        int duration = booking.getService().getDurationMinutes();
        LocalDateTime oldEnd = oldStart.plusMinutes(duration);
        LocalDateTime newEnd = newTime.plusMinutes(duration);

        freeUpOldTimeSlot(booking.getBarber(), oldStart, oldEnd);

        BarberSchedule newSchedule = barberScheduleRepository
                .findAvailableSlotForBooking(booking.getBarber(), newTime, newEnd)
                .orElseThrow(() -> new IllegalStateException("Selected time is not available"));

        updateScheduleAfterBooking(newSchedule, newTime, newEnd);

        booking.setAppointmentTime(newTime);
    }

    private void freeUpOldTimeSlot(Barber barber, LocalDateTime startTime, LocalDateTime endTime) {

        List<BarberSchedule> occupiedSlots = barberScheduleRepository
                .findByBarberAndBookedTrueAndEndTimeAfterAndStartTimeBefore(
                        barber, startTime, endTime);

        for (BarberSchedule slot : occupiedSlots) {

            if (slot.getStartTime().isBefore(startTime)) {
                BarberSchedule beforeSlot = BarberSchedule.builder()
                        .barber(barber)
                        .startTime(slot.getStartTime())
                        .endTime(startTime)
                        .available(true)
                        .booked(false)
                        .build();
                barberScheduleRepository.save(beforeSlot);
            }

            if (slot.getEndTime().isAfter(endTime)) {
                BarberSchedule afterSlot = BarberSchedule.builder()
                        .barber(barber)
                        .startTime(endTime)
                        .endTime(slot.getEndTime())
                        .available(true)
                        .booked(false)
                        .build();
                barberScheduleRepository.save(afterSlot);
            }

            barberScheduleRepository.delete(slot);
        }
    }
}

