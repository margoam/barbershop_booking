package com.teachmeskills.tms_booking_project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "barber_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Transient
    private Long barberId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "barber_id", nullable = false)
    @NotNull(message = "barber_id is required")
    private Barber barber;

    @FutureOrPresent
    @Column(name = "start_time", nullable = false)
    @NotNull(message = "start_time is required")
    private LocalDateTime startTime;

    @FutureOrPresent
    @Column(name = "end_time", nullable = false)
    @NotNull(message = "end_time is required")
    private LocalDateTime endTime;

    @NotNull(message = "is_available is required")
    @Column(name = "is_available", nullable = false)
    private boolean available = true;

    @NotNull(message = "is_booked is required")
    @Column(name = "is_booked", nullable = false)
    private boolean booked = false;

}
