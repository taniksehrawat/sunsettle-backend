package com.sunsettle.service;

import com.sunsettle.entity.MeterReading;
import com.sunsettle.repository.MeterReadingRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnergySummaryService {

    private final MeterReadingRepository meterReadingRepository;

    public EnergySummaryService(MeterReadingRepository meterReadingRepository) {
        this.meterReadingRepository = meterReadingRepository;
    }

    // Daily summary for a site
    public List<Object> getDailySummary(Long siteId) {

        List<MeterReading> readings = meterReadingRepository.findBySiteId(siteId);

        return readings.stream()
                .map(r -> {
                    double net = r.getExportKwh() - r.getImportKwh();
                    return new DailySummary(
                            r.getReadingDate(),
                            r.getGenerationKwh(),
                            r.getExportKwh(),
                            r.getImportKwh(),
                            net
                    );
                })
                .collect(Collectors.toList());
    }

    // DTO for response
    record DailySummary(
            LocalDate date,
            double generationKwh,
            double exportKwh,
            double importKwh,
            double netKwh
    ) {}
}
