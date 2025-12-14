package com.sunsettle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "meter_readings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeterReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate readingDate;

    private Double generationKwh;   // inverter generation
    private Double exportKwh;       // export to grid
    private Double importKwh;       // import from grid

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;
}
