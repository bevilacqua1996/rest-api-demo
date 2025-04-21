package com.jug.demo.mappers;

import com.jug.demo.entities.ClientEntity;
import com.jug.demo.generated.models.ClientRequest;
import com.jug.demo.generated.models.ClientResponse;

public class ClientMapper {

    public static ClientEntity toEntity(ClientRequest clientRequest) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setName(clientRequest.getName());
        clientEntity.setEmail(clientRequest.getEmail());
        return clientEntity;
    }

    public static ClientResponse toResponse(ClientEntity clientEntity) {
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(Math.toIntExact(clientEntity.getId()));
        clientResponse.setName(clientEntity.getName());
        clientResponse.setEmail(clientEntity.getEmail());
        return clientResponse;
    }
}