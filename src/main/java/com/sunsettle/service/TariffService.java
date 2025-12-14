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

    public TariffService(TariffRepository tariffRepository, SiteRepository siteRepository) {
        this.tariffRepository = tariffRepository;
        this.siteRepository = siteRepository;
    }

    public Tariff setTariff(Long siteId, Double rate) {
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));

        Tariff tariff = Tariff.builder()
                .ratePerKwh(rate)
                .site(site)
                .build();

        return tariffRepository.save(tariff);
    }

    public Tariff getTariff(Long siteId) {
        return tariffRepository.findBySiteId(siteId);
    }
}
