package com.sunsettle.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tariffs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double ratePerKwh; // tariff rate

    @OneToOne
    @JoinColumn(name = "site_id")
    private Site site;
}
