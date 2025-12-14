package com.sunsettle.repository;

import com.sunsettle.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    // Get single client based on user ID
    Client findByUserId(Long userId);

    // If in the future 1 user can have multiple clients
    List<Client> findAllByUserId(Long userId);
}
