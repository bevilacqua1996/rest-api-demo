package com.jug.demo.cucumber.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jug.demo.controllers.ClientController;
import com.jug.demo.services.ClientService;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

/**
 * This class configures the Spring context for Cucumber tests.
 * It uses @CucumberContextConfiguration to tell Cucumber to use this class for Spring context configuration.
 * It uses @WebMvcTest to set up a Spring MVC test context for the ClientController.
 */
@CucumberContextConfiguration
@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc
public class CucumberSpringConfiguration {

    @MockBean
    private ClientService clientService;
}