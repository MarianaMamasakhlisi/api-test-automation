Feature: User account operations - negative scenarios
  As an API consumer of the Swagger Petstore
  I want invalid user requests to fail clearly
  So that client applications can rely on predictable error responses

  Scenario: Retrieve a user that does not exist
    When I request a user with username "definitely_not_a_real_user_xyz"
    Then the response status code should be 404
    And the response body message should be "User not found"

  Scenario: Delete a user that does not exist
    When I delete a user with username "definitely_not_a_real_user_xyz"
    Then the response status code should be 404

  Scenario: Creating a user with a malformed request body
    When I send a malformed create user request
    Then the response status code should be 400
    And the response body message should be "bad input"

  Scenario: Creating users via the array endpoint with a malformed request body
    When I send a malformed create-with-array request
    Then the response status code should be 500
    And the response body message should be "something bad happened"

  Scenario: Creating users via the list endpoint with a malformed request body
    When I send a malformed create-with-list request
    Then the response status code should be 500
    And the response body message should be "something bad happened"
