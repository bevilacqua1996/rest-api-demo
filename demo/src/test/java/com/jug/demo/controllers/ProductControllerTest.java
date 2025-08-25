package com.jug.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jug.demo.generated.models.ProductRequest;
import com.jug.demo.generated.models.ProductResponse;
import com.jug.demo.generated.models.TaxRequest;
import com.jug.demo.generated.models.TaxResponse;
import com.jug.demo.services.ProductService;
import com.jug.demo.services.TaxCreditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ProductController Integration Tests")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private TaxCreditService taxCreditService;

    @BeforeEach
    void setUp() {
        // Reset mocks before each test
        reset(productService, taxCreditService);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public ProductService productService() {
            return mock(ProductService.class);
        }

        @Bean
        @Primary
        public TaxCreditService taxCreditService() {
            return mock(TaxCreditService.class);
        }
    }

    @Test
    @DisplayName("Should create a product successfully")
    void testCreateProduct() throws Exception {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setPrice(99.99f);
        productRequest.setClients(List.of(1));

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(1);
        productResponse.setName("Test Product");
        productResponse.setPrice(99.99f);
        productResponse.setClients(List.of(1));

        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);

        // Act & Assert
        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Product")))
                .andExpect(jsonPath("$.price", is(99.99)))
                .andExpect(jsonPath("$.clients", is(List.of(1))));

        verify(productService, times(1)).createProduct(any(ProductRequest.class));
    }

    @Test
    @DisplayName("Should return all products")
    void testGetAllProducts() throws Exception {
        // Arrange
        List<ProductResponse> products = Arrays.asList(
                createProductResponse(1, "Product 1", 10.99f, 1),
                createProductResponse(2, "Product 2", 20.99f, 2)
        );

        when(productService.getAllProducts()).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/product")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Product 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Product 2")));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("Should return a product by ID when it exists")
    void testGetProductById_WhenExists() throws Exception {
        // Arrange
        int productId = 1;
        ProductResponse productResponse = createProductResponse(productId, "Test Product", 99.99f, 1);

        when(productService.getProductById(productId)).thenReturn(Optional.of(productResponse));

        // Act & Assert
        mockMvc.perform(get("/product/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.name", is("Test Product")))
                .andExpect(jsonPath("$.price", is(99.99)))
                .andExpect(jsonPath("$.clients", is(List.of(1))));

        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    @DisplayName("Should return 404 when product does not exist")
    void testGetProductById_WhenNotExists() throws Exception {
        // Arrange
        int productId = 999;

        when(productService.getProductById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/product/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    @DisplayName("Should update a product successfully")
    void testUpdateProduct() throws Exception {
        // Arrange
        int productId = 1;
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Updated Product");
        productRequest.setPrice(149.99f);
        productRequest.setClients(List.of(1));

        ProductResponse productResponse = createProductResponse(productId, "Updated Product", 149.99f, 1);

        when(productService.updateProduct(eq(productId), any(ProductRequest.class))).thenReturn(productResponse);

        // Act & Assert
        mockMvc.perform(put("/product/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.name", is("Updated Product")))
                .andExpect(jsonPath("$.price", is(149.99)))
                .andExpect(jsonPath("$.clients", is(List.of(1))));

        verify(productService, times(1)).updateProduct(eq(productId), any(ProductRequest.class));
    }

    @Test
    @DisplayName("Should delete a product successfully")
    void testDeleteProduct() throws Exception {
        // Arrange
        int productId = 1;
        doNothing().when(productService).deleteProduct(productId);

        // Act & Assert
        mockMvc.perform(delete("/product/{id}", productId))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    @DisplayName("Should return a product report")
    void testGetProductReport() throws Exception {
        // Arrange
        int productId = 1;
        String report = "Product Report for ID: 1\nName: Test Product\nPrice: 99.99";

        when(productService.getProductReport(productId)).thenReturn(report);

        // Act & Assert
        mockMvc.perform(get("/product/{id}/report", productId)
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(report));

        verify(productService, times(1)).getProductReport(productId);
    }

    @Test
    @DisplayName("Should return tax details for a product")
    void testGetProductTaxDetails() throws Exception {
        // Arrange
        int productId = 1;
        TaxRequest taxRequest = new TaxRequest();
        taxRequest.setCountry("Brazil");

        ProductResponse productResponse = createProductResponse(productId, "Test Product", 100.0f, 1);

        TaxResponse taxResponse = new TaxResponse();
        taxResponse.setProductId(productId);
        taxResponse.setBasePrice(BigDecimal.valueOf(100.0));
        taxResponse.setTaxRate(BigDecimal.valueOf(0.10));
        taxResponse.setTaxAmount(BigDecimal.valueOf(10.0));
        taxResponse.setFinalPrice(BigDecimal.valueOf(110.0));

        when(productService.getProductById(productId)).thenReturn(Optional.of(productResponse));
        when(taxCreditService.calculateTaxCredit(eq("Brazil"), any(BigDecimal.class))).thenReturn(taxResponse);

        // Act & Assert
        mockMvc.perform(post("/product/{id}/tax-details", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taxRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId", is(productId)))
                .andExpect(jsonPath("$.basePrice", is(100.0)))
                .andExpect(jsonPath("$.taxRate", is(0.10)))
                .andExpect(jsonPath("$.taxAmount", is(10.0)))
                .andExpect(jsonPath("$.finalPrice", is(110.0)));

        verify(productService, times(1)).getProductById(productId);
        verify(taxCreditService, times(1)).calculateTaxCredit(eq("Brazil"), any(BigDecimal.class));
    }

    private ProductResponse createProductResponse(int id, String name, float price, int clientId) {
        ProductResponse response = new ProductResponse();
        response.setId(id);
        response.setName(name);
        response.setPrice(price);
        response.setClients(List.of(clientId));
        return response;
    }
}
