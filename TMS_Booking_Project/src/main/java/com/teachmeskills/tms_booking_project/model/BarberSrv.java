package com.teachmeskills.tms_booking_project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "service")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberSrv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotNull(message = "name is required")
    private String name;

    @Column(length = 200)
    @NotNull(message = "description is required")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "price is required")
    private BigDecimal price;

    @Column(nullable = false)
    @NotNull(message = "active is required")
    private boolean active;

    @Column(name = "duration", nullable = false)
    @NotNull(message = "duration is required")
    private int durationMinutes;

}
