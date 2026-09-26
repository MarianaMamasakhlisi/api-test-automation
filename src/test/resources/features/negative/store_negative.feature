Feature: Store order operations - negative scenarios
  As an API consumer of the Swagger Petstore
  I want invalid order requests to fail clearly
  So that client applications can rely on predictable error responses

  Scenario: Retrieve an order that does not exist
    When I request an order with id 999999999
    Then the response status code should be 404
    And the response body message should be "Order not found"

  Scenario: Delete an order that does not exist
    When I delete an order with id 999999999
    Then the response status code should be 404
    And the response body message should be "Order Not Found"

  Scenario: Retrieving an order with a non-numeric id
    When I request an order with a non-numeric id
    Then the response status code should be 404
    And the response body message should contain "NumberFormatException"

  Scenario: Deleting an order with a non-numeric id
    When I delete an order with a non-numeric id
    Then the response status code should be 404
    And the response body message should contain "NumberFormatException"

  Scenario: Placing an order with a malformed request body
    When I place an order with a malformed request body
    Then the response status code should be 400
    And the response body message should be "bad input"
