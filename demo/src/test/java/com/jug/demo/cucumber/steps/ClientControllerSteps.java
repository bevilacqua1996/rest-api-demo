package com.jug.demo.cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jug.demo.cucumber.config.CucumberSpringConfiguration;
import com.jug.demo.generated.models.ClientRequest;
import com.jug.demo.generated.models.ClientResponse;
import com.jug.demo.services.ClientService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Step definitions for the Client Controller BDD tests.
 * This class uses Spring's MockMvc to simulate HTTP requests to the ClientController.
 * The Spring context configuration is defined in CucumberSpringConfiguration.
 */
public class ClientControllerSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientService clientService;

    private ClientRequest clientRequest;
    private ResultActions resultActions;
    private ClientResponse mockClientResponse;

    @Given("I have a client with name {string} and email {string}")
    public void iHaveAClientWithNameAndEmail(String name, String email) {
        clientRequest = new ClientRequest();
        clientRequest.setName(name);
        clientRequest.setEmail(email);
    }

    @When("I send a POST request to {string} with the client data")
    public void iSendAPOSTRequestToWithTheClientData(String endpoint) throws Exception {
        // Setup mock response
        mockClientResponse = new ClientResponse();
        mockClientResponse.setId(1);
        mockClientResponse.setName(clientRequest.getName());
        mockClientResponse.setEmail(clientRequest.getEmail());

        when(clientService.createClient(any(ClientRequest.class))).thenReturn(mockClientResponse);

        // Perform request
        resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest))
        );
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) throws Exception {
        resultActions.andExpect(status().is(statusCode));
    }

    @And("the response should contain a client with name {string}")
    public void theResponseShouldContainAClientWithName(String name) throws Exception {
        resultActions.andExpect(jsonPath("$.name", is(name)));
    }

    @And("the response should contain a client with email {string}")
    public void theResponseShouldContainAClientWithEmail(String email) throws Exception {
        resultActions.andExpect(jsonPath("$.email", is(email)));
    }

    @And("the response should contain a client with an ID")
    public void theResponseShouldContainAClientWithAnID() throws Exception {
        resultActions.andExpect(jsonPath("$.id").exists());
    }

    @Given("there is a client with ID {int} in the system")
    public void thereIsAClientWithIDInTheSystem(int id) {
        mockClientResponse = new ClientResponse();
        mockClientResponse.setId(id);
        mockClientResponse.setName("John Doe");
        mockClientResponse.setEmail("john.doe@example.com");

        when(clientService.getClientById(id)).thenReturn(Optional.of(mockClientResponse));
    }

    @When("I send a GET request to {string}")
    public void iSendAGETRequestTo(String endpoint) throws Exception {
        resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @And("the response should contain a client with ID {int}")
    public void theResponseShouldContainAClientWithID(int id) throws Exception {
        resultActions.andExpect(jsonPath("$.id", is(id)));
    }

    @And("I have updated client data with name {string} and email {string}")
    public void iHaveUpdatedClientDataWithNameAndEmail(String name, String email) {
        clientRequest = new ClientRequest();
        clientRequest.setName(name);
        clientRequest.setEmail(email);

        // Setup mock response for update
        ClientResponse updatedResponse = new ClientResponse();
        updatedResponse.setId(1);
        updatedResponse.setName(name);
        updatedResponse.setEmail(email);

        when(clientService.updateClient(eq(1), any(ClientRequest.class))).thenReturn(updatedResponse);
    }

    @When("I send a PUT request to {string} with the updated client data")
    public void iSendAPUTRequestToWithTheUpdatedClientData(String endpoint) throws Exception {
        resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest))
        );
    }

    @When("I send a DELETE request to {string}")
    public void iSendADELETERequestTo(String endpoint) throws Exception {
        doNothing().when(clientService).deleteClient(1);

        resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
}
