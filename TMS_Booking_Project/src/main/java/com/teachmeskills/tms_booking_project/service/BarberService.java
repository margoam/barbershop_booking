package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.Barber;
import com.teachmeskills.tms_booking_project.repository.BarberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;
    private static final Logger logger = LoggerFactory.getLogger(BarberService.class);

    public List<Barber> getAll() {
        return barberRepository.findAll();
    }

    public Barber getById(Long id) {
        return barberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Barber not found"));
    }

    public Barber create(Barber barber) {
        logger.info("Created barber with id: {}", barber.getId());
        return barberRepository.save(barber);
    }

    @Transactional
    public Optional<Barber> update(Long id, Barber updatedBarber) {
        Barber existing = getById(id);
        existing.setFullName(updatedBarber.getFullName());
        existing.setEmail(updatedBarber.getEmail());
        existing.setPhone(updatedBarber.getPhone());
        existing.setRating(updatedBarber.getRating());
        logger.info("Updated barber with id: {}", existing.getId());
        return Optional.of(barberRepository.save(existing));
    }

    public boolean delete(Long id) {
        barberRepository.deleteById(id);
        logger.info("Deleted barber with id: {}", id);
        return true;
    }
}
