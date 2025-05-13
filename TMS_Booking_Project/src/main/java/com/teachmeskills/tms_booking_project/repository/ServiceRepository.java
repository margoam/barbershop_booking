package com.teachmeskills.tms_booking_project.repository;

import com.teachmeskills.tms_booking_project.model.BarberSrv;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<BarberSrv, Long> {
}
