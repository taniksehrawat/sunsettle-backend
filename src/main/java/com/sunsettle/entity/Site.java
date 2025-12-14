package com.sunsettle.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String siteName;

    private String location;

    private Double capacityKw;   // plant capacity in kW

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
