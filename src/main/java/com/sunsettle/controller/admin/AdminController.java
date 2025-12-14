package com.sunsettle.controller.admin;

import com.sunsettle.entity.Client;
import com.sunsettle.entity.Site;
import com.sunsettle.service.ClientService;
import com.sunsettle.service.SiteService;
import com.sunsettle.service.MeterReadingService;
import com.sunsettle.service.BillingService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final ClientService clientService;
    private final SiteService siteService;
    private final MeterReadingService meterReadingService;
    private final BillingService billingService;

    public AdminController(ClientService clientService,
                           SiteService siteService,
                           MeterReadingService meterReadingService,
                           BillingService billingService) {
        this.clientService = clientService;
        this.siteService = siteService;
        this.meterReadingService = meterReadingService;
        this.billingService = billingService;
    }

    // 1️⃣ Create Client & Assign User (Client Login Owner)
    @PostMapping("/client/{userId}")
    public Client createClient(@PathVariable Long userId,
                               @RequestBody Client client) {
        return clientService.addClient(client, userId);
    }

    // 2️⃣ View All Clients
    @GetMapping("/clients")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    // 3️⃣ Create Site for a Client
    @PostMapping("/site/{clientId}")
    public Site createSite(@PathVariable Long clientId,
                           @RequestBody Site site) {
        return siteService.createSite(clientId, site);
    }

    // 4️⃣ View All Sites
    @GetMapping("/sites")
    public List<Site> getAllSites() {
        return siteService.getAllSites();
    }

    // 5️⃣ Upload Meter Readings CSV
    @PostMapping("/meter/upload/{siteId}")
    public Object uploadMeter(@PathVariable Long siteId,
                              @RequestParam("file") MultipartFile file) {
        return meterReadingService.uploadCsv(siteId, file);
    }

    // 6️⃣ Generate Monthly Bill
    @GetMapping("/bill/{siteId}")
    public Object generateBill(@PathVariable Long siteId,
                               @RequestParam int month,
                               @RequestParam int year) {
        return billingService.generateBill(siteId, month, year);
    }
}
