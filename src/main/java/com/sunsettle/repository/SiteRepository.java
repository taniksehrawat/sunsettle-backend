package com.sunsettle.repository;

import com.sunsettle.entity.Site;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Long> {
    List<Site> findByClient_User_Id(Long userId);
    Site findByIdAndClient_User_Id(Long siteId, Long userId);

}
