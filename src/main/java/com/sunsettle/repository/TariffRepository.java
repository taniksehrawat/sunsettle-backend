package com.sunsettle.repository;

import com.sunsettle.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffRepository extends JpaRepository<Tariff, Long> {
    Tariff findBySiteId(Long siteId);
}
