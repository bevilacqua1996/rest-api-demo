package com.jug.demo.demo.controllers;

import com.jug.demo.demo.entities.ClientEntity;
import com.jug.demo.demo.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEntity() {
        // Arrange
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);
        clientEntity.setName("Test Entity");

        when(clientService.createClient(any(ClientEntity.class))).thenReturn(clientEntity);

        // Act
        ResponseEntity<ClientEntity> response = clientController.createEntity(clientEntity);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Test Entity", response.getBody().getName());
        verify(clientService, times(1)).createClient(clientEntity);
    }

    @Test
    void testGetAllEntities() {
        // Arrange
        List<ClientEntity> entityList = new ArrayList<>();
        entityList.add(new ClientEntity(1L, "Entity 1", "Test 1"));
        entityList.add(new ClientEntity(2L, "Entity 2", "Test 2"));

        when(clientService.getAllClients()).thenReturn(entityList);

        // Act
        ResponseEntity<List<ClientEntity>> response = clientController.getAllEntities();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void testGetEntityById_WhenEntityExists() {
        // Arrange
        ClientEntity clientEntity = new ClientEntity(1L, "Test Entity", "Test Surname");
        when(clientService.getClientById(1L)).thenReturn(Optional.of(clientEntity));

        // Act
        ResponseEntity<ClientEntity> response = clientController.getEntityById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    void testGetEntityById_WhenEntityDoesNotExist() {
        // Arrange
        when(clientService.getClientById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ClientEntity> response = clientController.getEntityById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    void testUpdateEntity_WhenEntityExists() {
        // Arrange
        ClientEntity clientEntity = new ClientEntity(1L, "Updated Entity", "Updated Surname");
        when(clientService.getClientById(1L)).thenReturn(Optional.of(clientEntity));
        when(clientService.updateClient(eq(1L), any(ClientEntity.class))).thenReturn(clientEntity);

        // Act
        ResponseEntity<ClientEntity> response = clientController.updateEntity(1L, clientEntity);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Entity", response.getBody().getName());
        verify(clientService, times(1)).getClientById(1L);
        verify(clientService, times(1)).updateClient(eq(1L), any(ClientEntity.class));
    }

    @Test
    void testUpdateEntity_WhenEntityDoesNotExist() {
        // Arrange
        ClientEntity clientEntity = new ClientEntity(1L, "Updated Entity", "Updated Surname");
        when(clientService.getClientById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ClientEntity> response = clientController.updateEntity(1L, clientEntity);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clientService, times(1)).getClientById(1L);
        verify(clientService, times(0)).updateClient(anyLong(), any(ClientEntity.class));
    }

    @Test
    void testDeleteEntity_WhenEntityExists() {
        // Arrange
        when(clientService.getClientById(1L)).thenReturn(Optional.of(new ClientEntity()));

        // Act
        ResponseEntity<Void> response = clientController.deleteEntity(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clientService, times(1)).getClientById(1L);
        verify(clientService, times(1)).deleteClient(1L);
    }

    @Test
    void testDeleteEntity_WhenEntityDoesNotExist() {
        // Arrange
        when(clientService.getClientById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> response = clientController.deleteEntity(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clientService, times(1)).getClientById(1L);
        verify(clientService, times(0)).deleteClient(anyLong());
    }
}