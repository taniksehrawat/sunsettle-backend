package com.sunsettle.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tariffs")
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rate_per_kwh", nullable = false)
    private Double ratePerKwh;

    @OneToOne
    @JoinColumn(name = "site_id", nullable = false, unique = true)
    private Site site;

    public Long getId() {
        return id;
    }

    public Double getRatePerKwh() {
        return ratePerKwh;
    }

    public void setRatePerKwh(Double ratePerKwh) {
        this.ratePerKwh = ratePerKwh;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
