package com.sunsettle.controller;

import com.sunsettle.dto.DailyEnergySummaryDTO;
import com.sunsettle.dto.MonthlyEnergySummaryDTO;
import com.sunsettle.entity.MeterReading;
import com.sunsettle.service.MeterReadingService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")

public class MeterReadingController {

    private final MeterReadingService meterReadingService;

    public MeterReadingController(MeterReadingService meterReadingService) {
        this.meterReadingService = meterReadingService;
    }

    /* --------------------------------------------------------
     * CSV UPLOAD
     * -------------------------------------------------------- */
    @PostMapping("/meter/upload/{siteId}")
    public List<MeterReading> uploadCsv(
            @PathVariable Long siteId,
            @RequestParam("file") MultipartFile file
    ) {
        return meterReadingService.uploadCsv(siteId, file);
    }

    /* --------------------------------------------------------
     * RAW READINGS
     * -------------------------------------------------------- */
    @GetMapping("/meter/{siteId}")
    public List<MeterReading> getReadings(@PathVariable Long siteId) {
        return meterReadingService.getReadings(siteId);
    }

    /* --------------------------------------------------------
     * DAILY SUMMARY
     * -------------------------------------------------------- */
    @GetMapping("/readings/daily-summary/{siteId}")
    public List<DailyEnergySummaryDTO> getDailySummary(@PathVariable Long siteId) {
        return meterReadingService.getDailySummary(siteId);
    }

    /* --------------------------------------------------------
     * MONTHLY SUMMARY — Current Month
     * -------------------------------------------------------- */
    @GetMapping("/readings/monthly-summary/{siteId}")
    public MonthlyEnergySummaryDTO getMonthlySummary(@PathVariable Long siteId) {
        return meterReadingService.getMonthlySummary(siteId);
    }

    /* --------------------------------------------------------
     * MONTHLY SUMMARY — Specific Month/Year
     * -------------------------------------------------------- */
    @GetMapping("/readings/monthly-summary/{siteId}/{month}/{year}")
    public MonthlyEnergySummaryDTO getMonthlySummarySpecific(
            @PathVariable Long siteId,
            @PathVariable int month,
            @PathVariable int year
    ) {
        return meterReadingService.getMonthlySummary(siteId, month, year);
    }
}
