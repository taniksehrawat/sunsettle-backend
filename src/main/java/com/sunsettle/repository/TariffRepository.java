package com.sunsettle.repository;

import com.sunsettle.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {

    Tariff findBySite_Id(Long siteId);
}
