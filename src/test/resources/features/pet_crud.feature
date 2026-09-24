Feature: Pet CRUD operations
  As an API consumer of the Swagger Petstore
  I want to create, read, update and delete pets
  So that I can manage the pet catalog through the REST API

  Scenario: Create a new pet
    When I create a pet named "Buddy" with status "available"
    Then the response status code should be 200
    And the response content type should be "application/json"
    And the response pet should have name "Buddy" and status "available"

  Scenario: Retrieve an existing pet by id
    Given a pet named "Rex" with status "available" has been created
    When I request that pet by its id
    Then the response status code should be 200
    And the response pet should have name "Rex" and status "available"

  Scenario: Update an existing pet's name and status
    Given a pet named "Max" with status "available" has been created
    When I update that pet's name to "Max the Second" and status to "sold"
    Then the response status code should be 200
    And the response pet should have name "Max the Second" and status "sold"
    And requesting that pet again should return name "Max the Second" and status "sold"

  Scenario: Delete an existing pet
    Given a pet named "Bella" with status "available" has been created
    When I delete that pet
    Then the response status code should be 200
    And requesting that pet again should return status code 404

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
