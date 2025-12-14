package com.sunsettle.service;

import com.sunsettle.entity.Client;
import com.sunsettle.entity.Site;
import com.sunsettle.repository.ClientRepository;
import com.sunsettle.repository.SiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {

    private final SiteRepository siteRepository;
    private final ClientRepository clientRepository;

    public SiteService(SiteRepository siteRepository, ClientRepository clientRepository) {
        this.siteRepository = siteRepository;
        this.clientRepository = clientRepository;
    }

    public Site createSite(Long clientId, Site site) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        site.setClient(client);

        return siteRepository.save(site);
    }

    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }

    public List<Site> getSitesByClient(Long clientId) {
        return siteRepository.findAll()
                .stream()
                .filter(s -> s.getClient().getId().equals(clientId))
                .toList();
    }
    public Site getSiteForClient(Long siteId, Long userId) {
        return siteRepository.findByIdAndClient_User_Id(siteId, userId);
    }

}
