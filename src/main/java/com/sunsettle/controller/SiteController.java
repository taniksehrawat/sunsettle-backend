package com.sunsettle.controller;

import com.sunsettle.entity.Site;
import com.sunsettle.service.SiteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SiteController {

    private final SiteService siteService;

    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }

    // ✅ CREATE SITE FOR CLIENT
    @PostMapping("/clients/{clientId}/sites")
    public Site createSite(
            @PathVariable Long clientId,
            @RequestBody Site site
    ) {
        return siteService.createSite(clientId, site);
    }

    // ✅ GET ALL SITES (DEMO / ADMIN VIEW)
    @GetMapping("/sites")
    public List<Site> getAllSites() {
        return siteService.getAllSites();
    }

    // ✅ GET SITES FOR A CLIENT
    @GetMapping("/clients/{clientId}/sites")
    public List<Site> getSitesByClient(@PathVariable Long clientId) {
        return siteService.getSitesByClient(clientId);
    }
}
