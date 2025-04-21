package com.jug.demo.controllers;

import com.jug.demo.generated.models.ClientRequest;
import com.jug.demo.generated.models.ClientResponse;
import com.jug.demo.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("createClient should return CREATED status when client is successfully created")
    void createClientShouldReturnCreatedStatus() {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setName("John Doe");
        clientRequest.setEmail("john.doe@example.com");

        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(1);
        clientResponse.setName("John Doe");
        clientResponse.setEmail("john.doe@example.com");

        when(clientService.createClient(clientRequest)).thenReturn(clientResponse);

        ResponseEntity<ClientResponse> response = clientController.createClient(clientRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(clientResponse, response.getBody());
        verify(clientService, times(1)).createClient(clientRequest);
    }

    @Test
    @DisplayName("getClientList should return OK status with a list of clients")
    void getClientListShouldReturnOkStatus() {
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(1);
        clientResponse.setName("John Doe");
        clientResponse.setEmail("john.doe@example.com");

        when(clientService.getAllClients()).thenReturn(List.of(clientResponse));

        ResponseEntity<List<ClientResponse>> response = clientController.getClientList();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(clientService, times(1)).getAllClients();
    }

    @Test
    @DisplayName("getClientById should return OK status when client is found")
    void getClientByIdShouldReturnOkStatus() {
        int clientId = 1;
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(clientId);
        clientResponse.setName("John Doe");
        clientResponse.setEmail("john.doe@example.com");

        when(clientService.getClientById(clientId)).thenReturn(Optional.of(clientResponse));

        ResponseEntity<ClientResponse> response = clientController.getClientById(clientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clientResponse, response.getBody());
        verify(clientService, times(1)).getClientById(clientId);
    }

    @Test
    @DisplayName("getClientById should return NOT FOUND status when client is not found")
    void getClientByIdShouldReturnNotFoundStatus() {
        int clientId = 1;

        when(clientService.getClientById(clientId)).thenReturn(Optional.empty());

        ResponseEntity<ClientResponse> response = clientController.getClientById(clientId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clientService, times(1)).getClientById(clientId);
    }

    @Test
    @DisplayName("updateClient should return OK status when client is successfully updated")
    void updateClientShouldReturnOkStatus() {
        int clientId = 1;
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setName("John Doe Updated");
        clientRequest.setEmail("john.doe.updated@example.com");

        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(clientId);
        clientResponse.setName("John Doe Updated");
        clientResponse.setEmail("john.doe.updated@example.com");

        when(clientService.getClientById(clientId)).thenReturn(Optional.of(clientResponse));
        when(clientService.updateClient(clientId, clientRequest)).thenReturn(clientResponse);

        ResponseEntity<ClientResponse> response = clientController.updateClient(clientId, clientRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clientResponse, response.getBody());
        verify(clientService, times(1)).getClientById(clientId);
        verify(clientService, times(1)).updateClient(clientId, clientRequest);
    }

    @Test
    @DisplayName("updateClient should return NOT FOUND status when client does not exist")
    void updateClientShouldReturnNotFoundStatus() {
        int clientId = 1;
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setName("John Doe Updated");
        clientRequest.setEmail("john.doe.updated@example.com");

        when(clientService.getClientById(clientId)).thenReturn(Optional.empty());

        ResponseEntity<ClientResponse> response = clientController.updateClient(clientId, clientRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clientService, times(1)).getClientById(clientId);
        verify(clientService, never()).updateClient(anyInt(), any(ClientRequest.class));
    }

    @Test
    @DisplayName("deleteClient should return NO CONTENT status when client is successfully deleted")
    void deleteClientShouldReturnNoContentStatus() {
        int clientId = 1;

        when(clientService.getClientById(clientId)).thenReturn(Optional.of(new ClientResponse()));

        ResponseEntity<Void> response = clientController.deleteClient(clientId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clientService, times(1)).getClientById(clientId);
        verify(clientService, times(1)).deleteClient(clientId);
    }

    @Test
    @DisplayName("deleteClient should return NOT FOUND status when client does not exist")
    void deleteClientShouldReturnNotFoundStatus() {
        int clientId = 1;

        when(clientService.getClientById(clientId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = clientController.deleteClient(clientId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clientService, times(1)).getClientById(clientId);
        verify(clientService, never()).deleteClient(anyInt());
    }
}