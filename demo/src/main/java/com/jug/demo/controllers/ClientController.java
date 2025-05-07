package com.jug.demo.controllers;

import com.jug.demo.generated.controllers.ClientApi;
import com.jug.demo.generated.models.ClientRequest;
import com.jug.demo.generated.models.ClientResponse;
import com.jug.demo.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ClientController implements ClientApi {

    @Autowired
    private ClientService clientService;

    @Override
    public ResponseEntity<ClientResponse> createClient(ClientRequest clientRequest) {
        ClientResponse savedEntity = clientService.createClient(clientRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntity);
    }

    @Override
    public ResponseEntity<List<ClientResponse>> getClientList() {
        List<ClientResponse> entities = clientService.getAllClients();
        return ResponseEntity.ok(entities);
    }

    @Override
    public ResponseEntity<ClientResponse> getClientById(Integer id) {
        Optional<ClientResponse> entity = clientService.getClientById(id);
        return entity.map(ResponseEntity::ok).get();
    }

    @Override
    public ResponseEntity<ClientResponse> updateClient(Integer id, @Valid ClientRequest clientRequest) {
        clientRequest.setId(id); // Garante que o ID informado é mantido
        ClientResponse updatedEntity = clientService.updateClient(id, clientRequest);
        return ResponseEntity.ok(updatedEntity);
    }

    @Override
    public ResponseEntity<Void> deleteClient(Integer id) {
        clientService.deleteClient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<String> getClientReport(Integer id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.getClientReport(id));
    }
}