package com.jug.demo.services.impl;

import com.jug.demo.entities.ClientEntity;
import com.jug.demo.exceptions.ClientNotFoundException;
import com.jug.demo.generated.models.ClientRequest;
import com.jug.demo.generated.models.ClientResponse;
import com.jug.demo.mappers.ClientMapper;
import com.jug.demo.repositories.ClientRepository;
import com.jug.demo.services.ClientService;
import com.jug.demo.templates.GenerateClientReport;
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
        return Optional.of(clientRepository.findById(Long.valueOf(id))
                .map(ClientMapper::toResponse).orElseThrow(ClientNotFoundException::new));
    }

    @Override
    @Transactional
    public ClientResponse updateClient(Integer id, ClientRequest clientRequest) {
        if (!clientRepository.existsById(Long.valueOf(id))) {
            throw new ClientNotFoundException(id);
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
            throw new ClientNotFoundException(id);
        }
        clientRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public String getClientReport(Integer id) {
        return new GenerateClientReport(id, clientRepository).generate();
    }

}
