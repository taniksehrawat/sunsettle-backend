package com.sunsettle.controller.client;

import com.sunsettle.dto.BillDTO;
import com.sunsettle.entity.Site;
import com.sunsettle.entity.User;
import com.sunsettle.repository.SiteRepository;
import com.sunsettle.repository.UserRepository;
import com.sunsettle.config.JwtUtil;
import com.sunsettle.service.BillingService;
import com.sunsettle.service.EnergySummaryService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientPortalController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final SiteRepository siteRepository;
    private final BillingService billingService;
    private final EnergySummaryService energySummaryService;

    public ClientPortalController(JwtUtil jwtUtil,
                            UserRepository userRepository,
                            SiteRepository siteRepository,
                            BillingService billingService,
                            EnergySummaryService energySummaryService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.billingService = billingService;
        this.energySummaryService = energySummaryService;
    }

    // --------------------------
    // 1️⃣ GET CLIENT'S OWN SITES
    // --------------------------
    @GetMapping("/my-sites")
    public List<Site> getMySites(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email);

        return siteRepository.findByClient_User_Id(user.getId());
    }

    // -----------------------------------------
    // 2️⃣ GET ONE BILL FOR SPECIFIC SITE + MONTH
    // -----------------------------------------
    @GetMapping("/my-bill/{siteId}")
    public BillDTO getMyBill(@PathVariable Long siteId,
                             @RequestParam int month,
                             @RequestParam int year,
                             HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email);

        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));

        if (!site.getClient().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not allowed — Site does not belong to this user");
        }

        return billingService.generateBill(siteId, month, year);
    }

    // ---------------------------------------
    // 3️⃣ DAILY ENERGY SUMMARY FOR A SITE
    // ---------------------------------------
    @GetMapping("/my-energy/{siteId}")
    public Object getMyEnergy(@PathVariable Long siteId,
                              HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email);

        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));

        if (!site.getClient().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not allowed — Site does not belong to this user");
        }

        return energySummaryService.getDailySummary(siteId);
    }

    // ---------------------------------------
    // 4️⃣ GET MY BILL HISTORY (USED BY FRONTEND)
    // ---------------------------------------
    @GetMapping("/bills")
    public List<BillDTO> getMyBills(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email);

        Long clientId = user.getId();

        return billingService.getBillsByClient(clientId);
    }
}
