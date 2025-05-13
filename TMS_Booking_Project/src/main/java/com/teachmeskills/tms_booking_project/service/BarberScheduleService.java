package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.Barber;
import com.teachmeskills.tms_booking_project.model.BarberSchedule;
import com.teachmeskills.tms_booking_project.model.BarberSrv;
import com.teachmeskills.tms_booking_project.model.dto.AvailableSlotsResponse;
import com.teachmeskills.tms_booking_project.model.dto.BarberScheduleUpdateRequest;
import com.teachmeskills.tms_booking_project.repository.BarberRepository;
import com.teachmeskills.tms_booking_project.repository.BarberScheduleRepository;
import com.teachmeskills.tms_booking_project.repository.ServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.teachmeskills.tms_booking_project.constant.Constant.WORKING_HOUR_END;
import static com.teachmeskills.tms_booking_project.constant.Constant.WORKING_HOUR_START;

@Service

public class BarberScheduleService {

    private final BarberScheduleRepository barberScheduleRepository;
    private final BarberRepository barberRepository;
    private final ServiceRepository serviceRepository;
    private static final Logger logger = LoggerFactory.getLogger(BarberScheduleService.class);

    @Autowired
    public BarberScheduleService(BarberScheduleRepository barberScheduleRepository, BarberRepository barberRepository, ServiceRepository serviceRepository) {
        this.barberScheduleRepository = barberScheduleRepository;
        this.barberRepository = barberRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<BarberSchedule> getAll() {
        return barberScheduleRepository.findAll();
    }

    public BarberSchedule getById(Long id) {
        return barberScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
    }

    @Transactional
    public BarberSchedule create(BarberSchedule schedule) {
        if (schedule.getBarberId() == null) {
            throw new IllegalArgumentException("barber_id is required");
        }

        Barber barber = barberRepository.findById(schedule.getBarberId())
                .orElseThrow(() -> new EntityNotFoundException("Barber not found with id: " + schedule.getBarberId()));

        validateTimeSlot(schedule.getStartTime());
        validateTimeSlot(schedule.getEndTime());
        checkTimeSlotAvailability(barber.getId(), schedule.getStartTime(), schedule.getEndTime());

        schedule.setBarber(barber);
        logger.info("Creating new schedule: {}", schedule);
        return barberScheduleRepository.save(schedule);
    }

    @Transactional
    public BarberSchedule update(Long id, BarberScheduleUpdateRequest updated) {
        BarberSchedule existing = barberScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule is not found with id: " + id));

        boolean hasChanged = false;

        if (updated.startTime().isAfter(updated.endTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        validateTimeSlot(updated.startTime());
        validateTimeSlot(updated.endTime());

        if (!updated.startTime().isEqual(existing.getStartTime()) && !updated.endTime().isEqual(existing.getEndTime())) {
            checkTimeSlotAvailability(existing.getBarber().getId(),
                    updated.startTime(),
                    updated.endTime());
            existing.setStartTime(updated.startTime());
            existing.setEndTime(updated.endTime());
            existing.setAvailable(updated.isAvailable());
            existing.setBooked(updated.isBooked());
            hasChanged = true;
        }

        if (!hasChanged) {
            logger.info("No changes detected for schedule {}", id);
            throw new IllegalArgumentException("No changes detected");
        }

        logger.info("Updated schedule with id: {}", existing.getId());
        return barberScheduleRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        barberScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        barberScheduleRepository.deleteById(id);
        logger.info("Deleted schedule with id: {}", id);
    }

    public List<AvailableSlotsResponse> getAvailableSlots(Long barberId, Long serviceId) {

        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new EntityNotFoundException("Barber not found"));

        BarberSrv service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));

        int requiredMinutes = service.getDurationMinutes();
        LocalDateTime now = LocalDateTime.now();

        return barberScheduleRepository
                .findByBarberAndAvailableTrueAndBookedFalseAndStartTimeAfter(barber, now)
                .stream()
                .filter(s -> s.getEndTime().isAfter(s.getStartTime().plusMinutes(requiredMinutes)))
                .map(s -> new AvailableSlotsResponse(
                        s.getId(),
                        s.getStartTime(),
                        s.getEndTime()
                ))
                .toList();
    }

    private void validateTimeSlot(LocalDateTime time) {

        if (time == null) {
            throw new IllegalArgumentException("Time is required");
        }
        if (time.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Time must be in the future");
        }
        if (time.getDayOfWeek() == DayOfWeek.SUNDAY || time.getDayOfWeek() == DayOfWeek.MONDAY) {
            throw new IllegalArgumentException("No bookings on Sunday or Monday");
        }
        int hour = time.getHour();
        if (hour < WORKING_HOUR_START || hour >= WORKING_HOUR_END) {
            throw new IllegalArgumentException(
                    String.format("Time must be between %d:00 and %d:00",
                            WORKING_HOUR_START, WORKING_HOUR_END));
        }
        if (time.getMinute() != 0 && time.getMinute() != 30) {
            throw new IllegalArgumentException(
                    "Schedules are only available at :00 or :30 minutes");
        }
    }

    private void checkTimeSlotAvailability(Long barberId, LocalDateTime start, LocalDateTime end) {
        if (barberScheduleRepository.existsByBarberIdAndTimeRange(barberId, start, end)) {
            throw new IllegalArgumentException("New time slot overlaps with existing schedule");
        }

        Duration duration = Duration.between(start, end);
        if (duration.toMinutes() < 30) {
            throw new IllegalArgumentException("Minimum slot duration is 30 minutes");
        }
    }
}

