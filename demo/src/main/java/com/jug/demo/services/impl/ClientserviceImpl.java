package com.jug.demo.services.impl;

import com.jug.demo.entities.ClientEntity;
import com.jug.demo.generated.models.ClientRequest;
import com.jug.demo.generated.models.ClientResponse;
import com.jug.demo.mappers.ClientMapper;
import com.jug.demo.repositories.ClientRepository;
import com.jug.demo.services.ClientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientserviceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    @Transactional
    public ClientResponse createClient(ClientRequest clientRequest) {
        ClientEntity clientEntity = ClientMapper.toEntity(clientRequest);
        ClientEntity savedEntity = clientRepository.save(clientEntity);
        return ClientMapper.toResponse(savedEntity);
    }

    @Override
    public List<ClientResponse> getAllClients() {
        return clientRepository.findAll().stream()
                .map(ClientMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<ClientResponse> getClientById(Integer id) {
        return clientRepository.findById(Long.valueOf(id))
                .map(ClientMapper::toResponse);
    }

    @Override
    @Transactional
    public ClientResponse updateClient(Integer id, ClientRequest clientRequest) {
        if (!clientRepository.existsById(Long.valueOf(id))) {
            return null;
        }
        ClientEntity clientEntity = ClientMapper.toEntity(clientRequest);
        clientEntity.setId(Long.valueOf(id)); // Garante que o ID é mantido
        ClientEntity updatedEntity = clientRepository.save(clientEntity);
        return ClientMapper.toResponse(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteClient(Integer id) {
        if (!clientRepository.existsById(Long.valueOf(id))) {
            return; // Ou lance uma exceção, dependendo da lógica de negócio
        }
        clientRepository.deleteById(Long.valueOf(id));
    }

}
