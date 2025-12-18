package com.sunsettle.service;

import com.sunsettle.entity.Client;
import com.sunsettle.repository.ClientRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // =====================================================
    // DEMO / ADMIN: Create client (NO user mapping)
    // =====================================================
    public Client createClient(Client client) {
        client.setCreatedAt(LocalDateTime.now());
        return clientRepository.save(client);
    }

    // =====================================================
    // DEMO / ADMIN: Get all clients
    // =====================================================
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // =====================================================
    // OPTIONAL: Get client by ID
    // =====================================================
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }
}
