Feature: Pet CRUD operations - negative scenarios
  As an API consumer of the Swagger Petstore
  I want invalid requests to fail clearly
  So that client applications can rely on predictable error responses

  Scenario: Retrieve a pet that does not exist
    When I request a pet with id 999999999999
    Then the response status code should be 404
    And the response body message should be "Pet not found"

  Scenario: Delete a pet that does not exist
    When I delete a pet with id 999999999999
    Then the response status code should be 404

  Scenario: Create a pet with a malformed request body
    When I send a malformed create pet request
    Then the response status code should be 400
    And the response body message should be "bad input"
