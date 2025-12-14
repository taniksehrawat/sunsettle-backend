package com.sunsettle.controller;

import com.sunsettle.entity.Client;
import com.sunsettle.entity.User;
import com.sunsettle.service.ClientService;
import com.sunsettle.repository.UserRepository;

import com.sunsettle.config.JwtUtil;
import com.sunsettle.dto.BillDTO;
import com.sunsettle.service.BillingService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BillingService billingService;

    public ClientController(ClientService clientService,
                            UserRepository userRepository,
                            JwtUtil jwtUtil,
                            BillingService billingService) {
        this.clientService = clientService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.billingService = billingService;
    }

    // ==================================================
    // GET LOGGED-IN CLIENT PROFILE
    // ==================================================
    @GetMapping("/my-profile")
    public Client getMyProfile(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email);
        return clientService.getClientByUserId(user.getId());
    }

    // ==================================================
    // LIST ALL BILLS FOR LOGGED-IN CLIENT
    // Used by ClientDashboard → BillHistory component
    // ==================================================
    @GetMapping("/bills")
    public List<BillDTO> getMyBills(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email);

        Long clientId = clientService.getClientByUserId(user.getId()).getId();

        return billingService.getBillsByClient(clientId);
    }
}
