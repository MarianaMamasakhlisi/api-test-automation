@user @positive
Feature: User account operations - positive scenarios
  As an API consumer of the Swagger Petstore
  I want to manage user accounts and sessions
  So that customers can register and sign in

  @smoke
  Scenario: Create a new user
    When I create a user with a unique username
    Then the response status code should be 200

  @smoke
  Scenario: Retrieve a user by username
    Given a user has been created
    When I request that user by username
    Then the response status code should be 200
    And the response user should have the same username

  Scenario: Update a user's first name
    Given a user has been created
    When I update that user's first name to "Updated"
    Then the response status code should be 200
    And requesting that user again should return first name "Updated"

  @smoke
  Scenario: Delete a user
    Given a user has been created
    When I delete that user
    Then the response status code should be 200
    And requesting that user again should return status code 404

  Scenario: Log in and log out
    Given a user has been created
    When I log in as that user
    Then the response status code should be 200
    And the response should include a rate limit header
    When I log out
    Then the response status code should be 200

  Scenario: Create multiple users in a single request via the array endpoint
    When I create multiple users using the array endpoint
    Then the response status code should be 200

  Scenario: Create multiple users in a single request via the list endpoint
    When I create multiple users using the list endpoint
    Then the response status code should be 200
