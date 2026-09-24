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
