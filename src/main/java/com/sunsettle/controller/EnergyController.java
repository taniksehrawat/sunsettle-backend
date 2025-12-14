package com.sunsettle.controller;

import com.sunsettle.dto.DailyEnergySummaryDTO;
import com.sunsettle.dto.MonthlyEnergySummaryDTO;
import com.sunsettle.service.MeterReadingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/energy")

public class EnergyController {

    private final MeterReadingService meterReadingService;

    public EnergyController(MeterReadingService meterReadingService) {
        this.meterReadingService = meterReadingService;
    }

    @GetMapping("/daily/{siteId}")
    public List<DailyEnergySummaryDTO> getDailySummary(@PathVariable Long siteId) {
        return meterReadingService.getDailySummary(siteId);
    }

    @GetMapping("/monthly/{siteId}")
    public MonthlyEnergySummaryDTO getMonthlySummary(
            @PathVariable Long siteId,
            @RequestParam int month,
            @RequestParam int year) {
        return meterReadingService.getMonthlySummary(siteId, month, year);
    }
}
