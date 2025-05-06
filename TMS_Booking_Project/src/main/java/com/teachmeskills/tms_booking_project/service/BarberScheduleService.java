package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.Barber;
import com.teachmeskills.tms_booking_project.model.BarberSchedule;
import com.teachmeskills.tms_booking_project.model.BarberSrv;
import com.teachmeskills.tms_booking_project.model.dto.AvailableSlotsResponse;
import com.teachmeskills.tms_booking_project.repository.BarberRepository;
import com.teachmeskills.tms_booking_project.repository.BarberScheduleRepository;
import com.teachmeskills.tms_booking_project.repository.ServiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
    }

    public BarberSchedule create(BarberSchedule schedule) {
        logger.info("Created schedule with id: {}", schedule.getId());
        return barberScheduleRepository.save(schedule);
    }

    @Transactional
    public BarberSchedule update(Long id, BarberSchedule updated) {
        BarberSchedule existing = getById(id);
        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getEndTime());
        existing.setAvailable(updated.isAvailable());
        existing.setBooked(updated.isBooked());
        logger.info("Updated schedule with id: {}", existing.getId());
        return barberScheduleRepository.save(existing);
    }

    public boolean delete(Long id) {
        barberScheduleRepository.deleteById(id);
        logger.info("Deleted schedule with id: {}", id);
        return true;
    }

    public List<AvailableSlotsResponse> getAvailableSlots(Long barberId, Long serviceId) {
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new IllegalArgumentException("Barber not found"));

        BarberSrv service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

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
}

