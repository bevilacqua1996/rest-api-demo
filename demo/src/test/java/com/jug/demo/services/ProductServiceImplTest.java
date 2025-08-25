package com.jug.demo.services;

import com.jug.demo.entities.ClientEntity;
import com.jug.demo.entities.ProductEntity;
import com.jug.demo.exceptions.ClientNotFoundException;
import com.jug.demo.generated.models.ProductRequest;
import com.jug.demo.generated.models.ProductResponse;
import com.jug.demo.repositories.ClientRepository;
import com.jug.demo.repositories.ProductRepository;
import com.jug.demo.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ClientRepository clientRepository;

    @Test
    void testCreateProduct() {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setPrice(99.99f);
        productRequest.setClients(List.of(1));

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);
        clientEntity.setName("Test Client");
        clientEntity.setEmail("test@example.com");

        ProductEntity savedEntity = new ProductEntity();
        savedEntity.setId(1L);
        savedEntity.setName("Test Product");
        savedEntity.setPrice(99.99f);
        savedEntity.setClients(List.of(clientEntity));

        when(clientRepository.findById(1L)).thenReturn(Optional.of(clientEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(savedEntity);

        // Act
        ProductResponse response = productService.createProduct(productRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Test Product", response.getName());
        assertEquals(99.99f, response.getPrice());
        assertEquals(List.of(1), response.getClients());
        verify(clientRepository).findById(1L);
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    void testCreateProduct_ClientNotFound() {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setPrice(99.99f);
        productRequest.setClients(List.of(1));

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> productService.createProduct(productRequest));
        verify(clientRepository).findById(1L);
        verify(productRepository, never()).save(any(ProductEntity.class));
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);
        clientEntity.setName("Test Client");
        clientEntity.setEmail("test@example.com");

        List<ProductEntity> products = List.of(
                createProductEntity(1L, "Product 1", 10.99f, clientEntity),
                createProductEntity(2L, "Product 2", 20.99f, clientEntity)
        );

        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<ProductResponse> responses = productService.getAllProducts();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Product 1", responses.get(0).getName());
        assertEquals("Product 2", responses.get(1).getName());
        verify(productRepository).findAll();
    }

    @Test
    void testGetProductById() {
        // Arrange
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);
        clientEntity.setName("Test Client");
        clientEntity.setEmail("test@example.com");

        ProductEntity productEntity = createProductEntity(1L, "Test Product", 99.99f, clientEntity);

        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        // Act
        Optional<ProductResponse> response = productService.getProductById(1);

        // Assert
        assertTrue(response.isPresent());
        assertEquals("Test Product", response.get().getName());
        assertEquals(99.99f, response.get().getPrice());
        assertEquals(List.of(1), response.get().getClients());
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<ProductResponse> response = productService.getProductById(1);

        // Assert
        assertTrue(response.isEmpty());
        verify(productRepository).findById(1L);
    }

    @Test
    void testUpdateProduct() {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Updated Product");
        productRequest.setPrice(149.99f);
        productRequest.setClients(List.of(1));

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);
        clientEntity.setName("Test Client");
        clientEntity.setEmail("test@example.com");

        ProductEntity existingEntity = createProductEntity(1L, "Test Product", 99.99f, clientEntity);
        ProductEntity updatedEntity = createProductEntity(1L, "Updated Product", 149.99f, clientEntity);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(clientEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(updatedEntity);

        // Act
        ProductResponse response = productService.updateProduct(1, productRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Updated Product", response.getName());
        assertEquals(149.99f, response.getPrice());
        assertEquals(List.of(1), response.getClients());
        verify(productRepository).findById(1L);
        verify(clientRepository).findById(1L);
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Updated Product");
        productRequest.setPrice(149.99f);
        productRequest.setClients(List.of(1));

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productService.updateProduct(1, productRequest));
        verify(productRepository).findById(1L);
        verify(clientRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any(ProductEntity.class));
    }

    @Test
    void testUpdateProduct_ClientNotFound() {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Updated Product");
        productRequest.setPrice(149.99f);
        productRequest.setClients(List.of(1));

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);
        clientEntity.setName("Test Client");
        clientEntity.setEmail("test@example.com");

        ProductEntity existingEntity = createProductEntity(1L, "Test Product", 99.99f, clientEntity);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> productService.updateProduct(1, productRequest));
        verify(productRepository).findById(1L);
        verify(clientRepository).findById(1L);
        verify(productRepository, never()).save(any(ProductEntity.class));
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);

        // Act
        productService.deleteProduct(1);

        // Assert
        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productService.deleteProduct(1));
        verify(productRepository).existsById(1L);
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetProductReport() {
        // Arrange
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);
        clientEntity.setName("Test Client");
        clientEntity.setEmail("test@example.com");

        ProductEntity productEntity = createProductEntity(1L, "Test Product", 99.99f, clientEntity);

        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        // Act
        String report = productService.getProductReport(1);

        // Assert
        assertNotNull(report);
        assertTrue(report.contains("Product Report"));
    }

    private ProductEntity createProductEntity(Long id, String name, Float price, ClientEntity client) {
        ProductEntity entity = new ProductEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setPrice(price);
        entity.setClients(List.of(client));
        return entity;
    }
}