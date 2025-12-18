package com.sunsettle.service;

import com.sunsettle.entity.Site;
import com.sunsettle.entity.Tariff;
import com.sunsettle.repository.SiteRepository;
import com.sunsettle.repository.TariffRepository;

import jakarta.annotation.PostConstruct;
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

    // 🔥 DEBUG: confirms Tariff entity is loaded in JVM
    @PostConstruct
    public void init() {
        System.out.println("✅ TariffService initialized");
        System.out.println("✅ Tariff entity loaded: " + Tariff.class.getName());
    }

    public void setTariff(Long siteId, Double rate) {

        if (rate == null || rate <= 0) {
            throw new RuntimeException("Rate must be greater than 0");
        }

        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));

        // IMPORTANT: use correct repository method
        Tariff tariff = tariffRepository.findBySite_Id(siteId);

        if (tariff == null) {
            tariff = new Tariff();
            tariff.setSite(site);
        }

        tariff.setRatePerKwh(rate);
        tariffRepository.save(tariff);

        System.out.println("✅ TARIFF SAVED for siteId=" + siteId);
    }
}
