package com.sunsettle.service;

import com.sunsettle.dto.DailyEnergySummaryDTO;
import com.sunsettle.dto.MonthlyEnergySummaryDTO;
import com.sunsettle.entity.MeterReading;
import com.sunsettle.entity.Site;
import com.sunsettle.repository.MeterReadingRepository;
import com.sunsettle.repository.SiteRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MeterReadingService {

    private final MeterReadingRepository meterReadingRepository;
    private final SiteRepository siteRepository;

    public MeterReadingService(MeterReadingRepository meterReadingRepository, SiteRepository siteRepository) {
        this.meterReadingRepository = meterReadingRepository;
        this.siteRepository = siteRepository;
    }

    public List<MeterReading> uploadCsv(Long siteId, MultipartFile file) {
        List<MeterReading> readings = new ArrayList<>();

        try {
            Site site = siteRepository.findById(siteId)
                    .orElseThrow(() -> new RuntimeException("Site not found"));

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
            );
                CSVParser csvParser = new CSVParser(
                        reader,
                        CSVFormat.DEFAULT
                                .builder()
                                .setHeader()
                                .setSkipHeaderRecord(true)
                                .setIgnoreHeaderCase(true)
                                .setTrim(true)
                                .build()
                )) {

                for (CSVRecord record : csvParser) {

                    MeterReading reading = MeterReading.builder()
                            .readingDate(LocalDate.parse(record.get("date")))
                            .generationKwh(Double.parseDouble(record.get("generation_kwh")))
                            .exportKwh(Double.parseDouble(record.get("export_kwh")))
                            .importKwh(Double.parseDouble(record.get("import_kwh")))
                            .site(site)
                            .build();

                    readings.add(reading);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("CSV parse error: " + e.getMessage());
        }

        return meterReadingRepository.saveAll(readings);
    }


    public List<MeterReading> getReadings(Long siteId) {
        return meterReadingRepository.findAll();
    }
    public List<DailyEnergySummaryDTO> getDailySummary(Long siteId) {
        List<MeterReading> readings = meterReadingRepository.findAll();

        List<DailyEnergySummaryDTO> summary = new ArrayList<>();

        for (MeterReading m : readings) {
            if (!m.getSite().getId().equals(siteId)) continue;

            double netGen = m.getGenerationKwh() - m.getImportKwh();
            double cuf = (m.getGenerationKwh() / (m.getSite().getCapacityKw() * 24)) * 100;

            summary.add(
                    DailyEnergySummaryDTO.builder()
                            .date(m.getReadingDate().toString())
                            .generationKwh(m.getGenerationKwh())
                            .exportKwh(m.getExportKwh())
                            .importKwh(m.getImportKwh())
                            .netGenerationKwh(netGen)
                            .cuf(Double.valueOf(String.format("%.2f", cuf)))
                            .build()
            );
        }

        return summary;
    }
    public MonthlyEnergySummaryDTO getMonthlySummary(Long siteId, int month, int year) {

        List<MeterReading> readings = meterReadingRepository.findAll();

        double totalGen = 0, totalExp = 0, totalImp = 0, totalCuf = 0;
        int count = 0;

        for (MeterReading m : readings) {
            if (!m.getSite().getId().equals(siteId)) continue;
            if (m.getReadingDate().getMonthValue() != month) continue;
            if (m.getReadingDate().getYear() != year) continue;

            totalGen += m.getGenerationKwh();
            totalExp += m.getExportKwh();
            totalImp += m.getImportKwh();

            double cuf = (m.getGenerationKwh() / (m.getSite().getCapacityKw() * 24)) * 100;
            totalCuf += cuf;

            count++;
        }

        return MonthlyEnergySummaryDTO.builder()
                .month(month + "-" + year)
                .totalGeneration(totalGen)
                .totalExport(totalExp)
                .totalImport(totalImp)
                .netGeneration(totalGen - totalImp)
                .averageCuf(count > 0 ? totalCuf / count : 0)
                .build();
    }
    public MonthlyEnergySummaryDTO getMonthlySummary(Long siteId) {

    LocalDate start = LocalDate.now().withDayOfMonth(1);
    LocalDate end = LocalDate.now();

    List<MeterReading> readings = meterReadingRepository.findMonthlyReadings(siteId, start, end);

    double totalGen = readings.stream().mapToDouble(MeterReading::getGenerationKwh).sum();
    double totalExport = readings.stream().mapToDouble(MeterReading::getExportKwh).sum();
    double totalImport = readings.stream().mapToDouble(MeterReading::getImportKwh).sum();

    // CUF = (Total Generation / (Capacity * 24 * days_elapsed)) * 100
    double capacity = readings.isEmpty() ? 1 : readings.get(0).getSite().getCapacityKw();
    long days = end.getDayOfMonth();
    double maxPossible = capacity * 24 * days;
    double cuf = (totalGen / maxPossible) * 100;

    return MonthlyEnergySummaryDTO.builder()
            .siteId(siteId)
            .month(start.toString())
            .totalGeneration(totalGen)
            .totalExport(totalExport)
            .totalImport(totalImport)
            .averageCuf(Math.round(cuf * 100.0) / 100.0)
            .build();
}



}
