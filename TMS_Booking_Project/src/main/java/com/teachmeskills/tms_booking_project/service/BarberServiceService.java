package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.BarberSrv;
import com.teachmeskills.tms_booking_project.repository.ServiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarberServiceService {

    private final ServiceRepository serviceRepository;
    private static final Logger logger = LoggerFactory.getLogger(BarberServiceService.class);

    @Autowired
    public BarberServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<BarberSrv> getAll() {
        return serviceRepository.findAll();
    }

    public BarberSrv getById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
    }

    public BarberSrv create(BarberSrv service) {
        logger.info("Created service with id: {}", service.getId());
        return serviceRepository.save(service);
    }

    @Transactional
    public BarberSrv update(Long id, BarberSrv updatedService) {
        BarberSrv existing = getById(id);
        existing.setName(updatedService.getName());
        existing.setDescription(updatedService.getDescription());
        existing.setPrice(updatedService.getPrice());
        existing.setActive(updatedService.isActive());
        logger.info("Updated service with id: {}", existing.getId());
        return serviceRepository.save(existing);
    }

    public boolean delete(Long id) {
        serviceRepository.deleteById(id);
        logger.info("Removed service with id: {}", id);
        return true;
    }
}

