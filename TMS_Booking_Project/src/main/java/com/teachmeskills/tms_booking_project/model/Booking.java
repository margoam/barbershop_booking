package com.teachmeskills.tms_booking_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;

    @Column(name = "price_paid", nullable = false, precision = 20, scale = 0)
    private BigDecimal pricePaid;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "barber_id")
    private Barber barber;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
}

