package com.sunsettle.service;

import com.sunsettle.entity.User;
import com.sunsettle.repository.UserRepository;

import com.sunsettle.entity.Client;
import com.sunsettle.repository.ClientRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public ClientService(ClientRepository clientRepository,
                         UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    // =====================================================
    // ADMIN: Create client (without mapping to a user)
    // =====================================================
    public Client createClient(Client client) {
        client.setCreatedAt(LocalDateTime.now());
        return clientRepository.save(client);
    }

    // =====================================================
    // ADMIN: Get all clients
    // =====================================================
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // =====================================================
    // ADMIN: Get client by ID
    // =====================================================
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElse(null);
    }

    // =====================================================
    // ADMIN: Create client under a specific USER account
    // =====================================================
    public Client addClient(Client client, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        client.setUser(user);
        client.setCreatedAt(LocalDateTime.now());

        return clientRepository.save(client);
    }

    // =====================================================
    // CLIENT: Fetch client using linked USER ID (important)
    // Used for: bill history, my-sites, dashboard
    // =====================================================
    public Client getClientByUserId(Long userId) {
        return clientRepository.findByUserId(userId);
    }

    // (Optional) Get all clients of a user (for multi-client accounts)
    public List<Client> getClientsByUserId(Long userId) {
        return clientRepository.findAllByUserId(userId);
    }
}
