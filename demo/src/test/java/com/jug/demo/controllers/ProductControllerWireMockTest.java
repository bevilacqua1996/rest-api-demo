package com.jug.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.jug.demo.generated.models.ProductResponse;
import com.jug.demo.generated.models.TaxRequest;
import com.jug.demo.generated.models.TaxResponse;
import com.jug.demo.services.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(WireMockExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerWireMockTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    private WireMockServer wireMockServer;

    private ProductResponse productResponse = new ProductResponse();

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public ProductService productService() {
            return mock(ProductService.class);
        }
    }

    @BeforeEach
    void init() throws JsonProcessingException {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);

        productResponse.setId(1);
        productResponse.setName("Test Product");
        productResponse.setPrice(100.0F);

        WireMock.stubFor(get(urlEqualTo("/api/tax-credit/brl"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("0.10")));

        WireMock.stubFor(get(urlEqualTo("/api/tax-credit/mzn"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("0.15")));

        WireMock.stubFor(get(urlEqualTo("/api/tax-credit/eur"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("0.20")));
    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }

    @Test
    void testTaxCreditBrazil() {

        TaxRequest taxRequest = new TaxRequest();
        taxRequest.setCountry("Brazil");

        when(productService.getProductById(1)).thenReturn(Optional.of(productResponse));

        ResponseEntity<TaxResponse> response = restTemplate.postForEntity("/product/1/tax-details", taxRequest, TaxResponse.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().getTaxRate().toString())
                .isEqualTo("0.10");
    }

    @Test
    void testTaxCreditMocambique() {

        TaxRequest taxRequest = new TaxRequest();
        taxRequest.setCountry("Mocambique");

        when(productService.getProductById(1)).thenReturn(Optional.of(productResponse));

        ResponseEntity<TaxResponse> response = restTemplate.postForEntity("/product/1/tax-details", taxRequest, TaxResponse.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().getTaxRate().toString())
                .isEqualTo("0.15");
    }

    @Test
    void testTaxCreditPortugal() {

        TaxRequest taxRequest = new TaxRequest();
        taxRequest.setCountry("Portugal");

        when(productService.getProductById(1)).thenReturn(Optional.of(productResponse));

        ResponseEntity<TaxResponse> response = restTemplate.postForEntity("/product/1/tax-details", taxRequest, TaxResponse.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().getTaxRate().toString())
                .isEqualTo("0.20");
    }
}