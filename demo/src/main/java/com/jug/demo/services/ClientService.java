package com.jug.demo.services;

import com.jug.demo.generated.models.ClientRequest;
import com.jug.demo.generated.models.ClientResponse;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    ClientResponse createClient(ClientRequest clientRequest);

    List<ClientResponse> getAllClients();

    Optional<ClientResponse> getClientById(Integer id);

    ClientResponse updateClient(Integer id, ClientRequest clientRequest);

    void deleteClient(Integer id);

    String getClientReport(Integer id);
}
