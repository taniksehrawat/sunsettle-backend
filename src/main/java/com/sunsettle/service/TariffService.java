package com.sunsettle.service;

import com.sunsettle.entity.Site;
import com.sunsettle.entity.Tariff;
import com.sunsettle.repository.SiteRepository;
import com.sunsettle.repository.TariffRepository;

import org.springframework.stereotype.Service;

@Service
public class TariffService {

    private final TariffRepository tariffRepository;
    private final SiteRepository siteRepository;

    public TariffService(TariffRepository tariffRepository,
                         SiteRepository siteRepository) {
        this.tariffRepository = tariffRepository;
        this.siteRepository = siteRepository;
    }

    /**
     * Set or Update Tariff for a Site (₹/kWh)
     * MVP-safe implementation
     */
    public void setTariff(Long siteId, Double rate) {

        if (rate == null || rate <= 0) {
            throw new RuntimeException("Tariff rate must be greater than 0");
        }

        // 1️⃣ Validate Site
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site not found for ID: " + siteId));

        try {
            // 2️⃣ Find existing tariff
            Tariff tariff = tariffRepository.findBySiteId(siteId);

            // 3️⃣ Create tariff if not exists
            if (tariff == null) {
                tariff = new Tariff();
                tariff.setSite(site);
            }

            // 4️⃣ Set rate
            tariff.setRatePerKwh(rate);

            // 5️⃣ Save
            tariffRepository.save(tariff);

        } catch (Exception e) {
            e.printStackTrace(); // IMPORTANT FOR RENDER LOGS
            throw new RuntimeException("Failed to set tariff: " + e.getMessage());
        }
    }
}
