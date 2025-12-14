package com.sunsettle.controller;

import com.sunsettle.entity.Site;
import com.sunsettle.entity.User;
import com.sunsettle.repository.SiteRepository;
import com.sunsettle.repository.UserRepository;
import com.sunsettle.config.JwtUtil;
import com.sunsettle.service.SiteService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping

public class SiteController {

    private final SiteService siteService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final SiteRepository siteRepository;

    public SiteController(SiteService siteService, 
                          JwtUtil jwtUtil,
                          UserRepository userRepository,
                          SiteRepository siteRepository) {
        this.siteService = siteService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
    }

    /* ---------------------------------------------------------
     *  ADMIN ENDPOINTS
     * --------------------------------------------------------- */

    // ADMIN: Create Site for Client
    

    // ADMIN: View all sites
    

    // ADMIN: View sites by Client ID
    


    /* ---------------------------------------------------------
     *  CLIENT ENDPOINTS
     * --------------------------------------------------------- */

    // CLIENT: Get all my sites (based on JWT)
    @GetMapping("/client/my-sites")
    public List<Site> getMySites(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email);

        return siteRepository.findByClient_User_Id(user.getId());
    }

    // CLIENT: Get one site WITH security (only if site belongs to client)
    @GetMapping("/client/site/{siteId}")
    public Site getMySiteDetails(@PathVariable Long siteId,
                                 HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email);

        return siteService.getSiteForClient(siteId, user.getId());
    }
}
