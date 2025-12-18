package com.sunsettle.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tariffs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double ratePerKwh;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "site_id",
        nullable = false,
        unique = true
    )
    private Site site;
}
