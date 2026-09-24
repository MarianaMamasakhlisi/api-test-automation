Feature: Store order operations - positive scenarios
  As an API consumer of the Swagger Petstore
  I want to place and manage orders
  So that customers can buy pets through the store

  Scenario: Retrieve store inventory
    When I request the store inventory
    Then the response status code should be 200
    And the inventory should report a count for status "available"

  Scenario: Place a new order
    When I place an order for pet id 12345 with quantity 2
    Then the response status code should be 200
    And the response order should have quantity 2

  Scenario: Retrieve an order by id
    Given an order for pet id 12345 with quantity 1 has been placed
    When I request that order by its id
    Then the response status code should be 200

  Scenario: Delete an order
    Given an order for pet id 12345 with quantity 1 has been placed
    When I delete that order
    Then the response status code should be 200
    And requesting that order again should return status code 404
