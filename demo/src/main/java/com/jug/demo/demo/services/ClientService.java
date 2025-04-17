package com.jug.demo.demo.services;

import com.jug.demo.demo.entities.ClientEntity;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    ClientEntity createClient(ClientEntity clientEntity);

    List<ClientEntity> getAllClients();

    Optional<ClientEntity> getClientById(Long id);

    ClientEntity updateClient(Long id, ClientEntity clientEntity);

    void deleteClient(Long id);
}
