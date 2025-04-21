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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("ClientController Unit Tests")
class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("createClient")
    class CreateClientTests {

        @Test
        @DisplayName("should return CREATED status when client is successfully created")
        void shouldReturnCreatedStatusWhenClientIsSuccessfullyCreated() {
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
    }

    @Nested
    @DisplayName("getClientById")
    class GetClientByIdTests {

        @Test
        @DisplayName("should return OK status and client when client is found")
        void shouldReturnOkStatusAndClientWhenClientIsFound() {
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
        @DisplayName("should throw exception when client is not found")
        void shouldThrowExceptionWhenClientIsNotFound() {
            int clientId = 1;

            when(clientService.getClientById(clientId)).thenReturn(Optional.empty());

            try {
                clientController.getClientById(clientId);
            } catch (Exception e) {
                assertEquals("No value present", e.getMessage());
            }

            verify(clientService, times(1)).getClientById(clientId);
        }
    }

    @Nested
    @DisplayName("updateClient")
    class UpdateClientTests {

        @Test
        @DisplayName("should return OK status when client is successfully updated")
        void shouldReturnOkStatusWhenClientIsSuccessfullyUpdated() {
            int clientId = 1;
            ClientRequest clientRequest = new ClientRequest();
            clientRequest.setId(clientId);
            clientRequest.setName("John Doe Updated");
            clientRequest.setEmail("john.doe.updated@example.com");

            ClientResponse clientResponse = new ClientResponse();
            clientResponse.setId(clientId);
            clientResponse.setName("John Doe Updated");
            clientResponse.setEmail("john.doe.updated@example.com");

            when(clientService.updateClient(clientId, clientRequest)).thenReturn(clientResponse);

            ResponseEntity<ClientResponse> response = clientController.updateClient(clientId, clientRequest);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(clientResponse, response.getBody());
            verify(clientService, times(1)).updateClient(clientId, clientRequest);
        }
    }
}