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

    public void setTariff(Long siteId, Double rate) {

        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));

        Tariff tariff = tariffRepository.findBySiteId(siteId);

        // 👉 If tariff doesn't exist, CREATE it (important for MVP)
        if (tariff == null) {
            tariff = new Tariff();
            tariff.setSite(site);
        }

        tariff.setRatePerKwh(rate);
        tariffRepository.save(tariff);
    }
}
