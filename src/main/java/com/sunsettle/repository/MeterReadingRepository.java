package com.sunsettle.repository;

import com.sunsettle.entity.MeterReading;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {
    List<MeterReading> findBySiteId(Long siteId);
    @Query("SELECT m FROM MeterReading m WHERE m.site.id = :siteId AND m.readingDate BETWEEN :start AND :end")
    List<MeterReading> findMonthlyReadings(Long siteId, LocalDate start, LocalDate end);

}
