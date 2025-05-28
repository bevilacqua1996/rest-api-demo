package com.jug.demo.services;

import com.jug.demo.entities.ClientEntity;
import com.jug.demo.entities.ClientEntityBuilder;
import com.jug.demo.entities.ClientRequestBuilder;
import com.jug.demo.generated.models.ClientRequest;
import com.jug.demo.generated.models.ClientResponse;
import com.jug.demo.repositories.ClientRepository;
import com.jug.demo.services.impl.ClientserviceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @InjectMocks
    private ClientserviceImpl clientService;

    @Mock
    private ClientRepository clientRepository;

    @Test
    void testCreateClient() {
        ClientRequest clientRequest = new ClientRequestBuilder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();


        ClientEntity savedEntity = new ClientEntityBuilder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        when(clientRepository.save(any(ClientEntity.class))).thenReturn(savedEntity);

        ClientResponse response = clientService.createClient(clientRequest);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("john.doe@example.com", response.getEmail());
    }

    @Test
    void testGetAllClients() {
        List<ClientEntity> entities = List.of(
                new ClientEntityBuilder().id(1L).name("John Doe").email("john.doe@example.com").build(),
                new ClientEntityBuilder().id(2L).name("Jane Doe").email("jane.doe@example.com").build()
        );

        when(clientRepository.findAll()).thenReturn(entities);

        List<ClientResponse> responses = clientService.getAllClients();

        assertNotNull(responses);
        assertEquals(2, responses.size());
    }

    @Test
    void testGetClientById() {
        ClientEntity clientEntity = new ClientEntityBuilder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(clientEntity));

        Optional<ClientResponse> response = clientService.getClientById(1);

        assertTrue(response.isPresent());
        assertEquals("John Doe", response.get().getName());
    }

    @Test
    void testUpdateClient() {
        ClientRequest clientRequest = new ClientRequestBuilder()
                .name("Updated Name")
                .email("updated.email@example.com")
                .build();

        ClientEntity existingEntity = new ClientEntityBuilder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        ClientEntity updatedEntity = new ClientEntityBuilder()
                .id(1L)
                .name("Updated Name")
                .email("updated.email@example.com")
                .build();

        when(clientRepository.existsById(1L)).thenReturn(true);
        when(clientRepository.save(any(ClientEntity.class))).thenReturn(updatedEntity);

        ClientResponse response = clientService.updateClient(1, clientRequest);

        assertNotNull(response);
        assertEquals("Updated Name", response.getName());
        assertEquals("updated.email@example.com", response.getEmail());
    }

    @Test
    void testDeleteClient() {
        when(clientRepository.existsById(1L)).thenReturn(true);

        clientService.deleteClient(1);

        verify(clientRepository, times(1)).deleteById(1L);
    }

//    @Test
//    void testGetClientReport() {
//        when(clientRepository.existsById(1L)).thenReturn(true);
//
//        String report = clientService.getClientReport(1);
//
//        assertNotNull(report);
//    }
}
