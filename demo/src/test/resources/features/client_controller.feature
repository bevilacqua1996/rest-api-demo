Feature: Client Controller API
  As a user of the API
  I want to manage clients
  So that I can keep track of client information

  Scenario: Create a new client
    Given I have a client with name "John Doe" and email "john.doe@example.com"
    When I send a POST request to "/client" with the client data
    Then the response status should be 201
    And the response should contain a client with name "John Doe"
    And the response should contain a client with email "john.doe@example.com"
    And the response should contain a client with an ID

  Scenario: Get a client by ID
    Given there is a client with ID 1 in the system
    When I send a GET request to "/client/1"
    Then the response status should be 200
    And the response should contain a client with ID 1
    And the response should contain a client with name "John Doe"
    And the response should contain a client with email "john.doe@example.com"

  Scenario: Update a client
    Given there is a client with ID 1 in the system
    And I have updated client data with name "Jane Doe" and email "jane.doe@example.com"
    When I send a PUT request to "/client/1" with the updated client data
    Then the response status should be 200
    And the response should contain a client with ID 1
    And the response should contain a client with name "Jane Doe"
    And the response should contain a client with email "jane.doe@example.com"

  Scenario: Delete a client
    Given there is a client with ID 1 in the system
    When I send a DELETE request to "/client/1"
    Then the response status should be 204