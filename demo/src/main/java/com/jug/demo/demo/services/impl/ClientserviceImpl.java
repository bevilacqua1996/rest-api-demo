package com.jug.demo.demo.services.impl;

import com.jug.demo.demo.entities.ClientEntity;
import com.jug.demo.demo.repositories.ClientRepository;
import com.jug.demo.demo.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientserviceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ClientEntity createClient(ClientEntity clientEntity) {
        return clientRepository.save(clientEntity);
    }

    @Override
    public List<ClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<ClientEntity> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public ClientEntity updateClient(Long id, ClientEntity clientEntity) {
        if (!clientRepository.existsById(id)) {
            return null; // Ou lance uma exceção, dependendo da lógica de negócio
        }
        clientEntity.setId(id); // Garante que o ID informado é mantido
        return clientRepository.save(clientEntity);
    }

    @Override
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            return; // Ou lance uma exceção, dependendo da lógica de negócio
        }
        clientRepository.deleteById(id);
    }

}
