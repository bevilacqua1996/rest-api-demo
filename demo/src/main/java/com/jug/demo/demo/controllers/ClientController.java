package com.jug.demo.demo.controllers;

import com.jug.demo.demo.entities.ClientEntity;
import com.jug.demo.demo.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/demo/v1") // Base URL para os endpoints
public class ClientController {

    @Autowired
    private ClientService clientService;

    // Endpoint para criar uma nova entidade (Create)
    @PostMapping("/client")
    public ResponseEntity<ClientEntity> createEntity(@RequestBody ClientEntity ClientEntity) {
        ClientEntity savedEntity = clientService.createClient(ClientEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntity);
    }

    // Endpoint para listar todas as entidades (Read - All)
    @GetMapping("/client")
    public ResponseEntity<List<ClientEntity>> getAllEntities() {
        List<ClientEntity> entities = clientService.getAllClients();
        return ResponseEntity.ok(entities);
    }

    // Endpoint para buscar uma entidade pelo ID (Read - Single)
    @GetMapping("/client/{id}")
    public ResponseEntity<ClientEntity> getEntityById(@PathVariable Long id) {
        Optional<ClientEntity> entity = clientService.getClientById(id);
        return entity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint para atualizar uma entidade existente (Update)
    @PutMapping("/client/{id}")
    public ResponseEntity<ClientEntity> updateEntity(@PathVariable Long id, @RequestBody ClientEntity clientEntity) {
        if (!clientService.getClientById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        clientEntity.setId(id); // Garante que o ID informado é mantido
        ClientEntity updatedEntity = clientService.updateClient(id, clientEntity);
        return ResponseEntity.ok(updatedEntity);
    }

    // Endpoint para deletar uma entidade pelo ID (Delete)
    @DeleteMapping("/client/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Long id) {
        if (!clientService.getClientById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        clientService.deleteClient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}