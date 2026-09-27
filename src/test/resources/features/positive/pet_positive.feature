@pet @positive
Feature: Pet CRUD operations - positive scenarios
  As an API consumer of the Swagger Petstore
  I want to create, read, update and delete pets
  So that I can manage the pet catalog through the REST API

  @smoke
  Scenario: Create a new pet
    When I create a pet named "Buddy" with status "available"
    Then the response status code should be 200
    And the response content type should be "application/json"
    And the response pet should have name "Buddy" and status "available"
    And the response should match the pet schema

  @smoke
  Scenario: Retrieve an existing pet by id
    Given a pet named "Rex" with status "available" has been created
    When I request that pet by its id
    Then the response status code should be 200
    And the response pet should have name "Rex" and status "available"

  @smoke
  Scenario: Update an existing pet's name and status
    Given a pet named "Max" with status "available" has been created
    When I update that pet's name to "Max the Second" and status to "sold"
    Then the response status code should be 200
    And the response pet should have name "Max the Second" and status "sold"
    And requesting that pet again should return name "Max the Second" and status "sold"

  @smoke
  Scenario: Delete an existing pet
    Given a pet named "Bella" with status "available" has been created
    When I delete that pet
    Then the response status code should be 200
    And requesting that pet again should return status code 404

  Scenario: Find pets by status
    When I search for pets with status "available"
    Then the response status code should be 200
    And every pet in the response should have status "available"

  Scenario: Find pets by tag
    When I search for pets with tag "friendly"
    Then the response status code should be 200

  Scenario: Update a pet's name and status using form data
    Given a pet named "FormPet" with status "available" has been created
    When I update that pet via form data to name "FormPetUpdated" and status "sold"
    Then the response status code should be 200
    And requesting that pet again should return name "FormPetUpdated" and status "sold"

  Scenario: Upload an image for a pet
    Given a pet named "PhotoPet" with status "available" has been created
    When I upload an image for that pet
    Then the response status code should be 200
    And the response body message should contain "File uploaded"

  Scenario: Find pets matching either of two statuses
    When I search for pets with statuses "available" and "pending"
    Then the response status code should be 200
    And every pet in the response should have status "available" or "pending"

  Scenario: Searching for pets with an unknown status returns no results
    When I search for pets with an unknown status
    Then the response status code should be 200
    And the response should be an empty list

  Scenario: Searching for pets with a tag that does not exist returns no results
    When I search for pets with a tag that does not exist
    Then the response status code should be 200
    And the response should be an empty list

  Scenario: Updating a pet id that was never created acts as an upsert
    When I update a pet id that was never created with name "Phantom" and status "available"
    Then the response status code should be 200
    And requesting that pet again should return name "Phantom" and status "available"
