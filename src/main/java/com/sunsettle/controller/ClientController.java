package com.sunsettle.controller;

import com.sunsettle.entity.Client;
import com.sunsettle.dto.BillDTO;
import com.sunsettle.service.ClientService;
import com.sunsettle.service.BillingService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    private final ClientService clientService;
    private final BillingService billingService;

    public ClientController(ClientService clientService,
                            BillingService billingService) {
        this.clientService = clientService;
        this.billingService = billingService;
    }

    // ==================================================
    // DEMO / ADMIN — CREATE CLIENT (NO AUTH)
    // ==================================================
    @PostMapping("/clients")
    public Client createClient(@RequestBody Client client) {
        return clientService.createClient(client);
    }

    // ==================================================
    // DEMO / ADMIN — GET ALL CLIENTS
    // ==================================================
    @GetMapping("/clients")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    // ==================================================
    // DEMO — GET BILLS FOR A CLIENT (NO LOGIN)
    // ==================================================
    @GetMapping("/clients/{clientId}/bills")
    public List<BillDTO> getBillsByClient(@PathVariable Long clientId) {
        return billingService.getBillsByClient(clientId);
    }
}
