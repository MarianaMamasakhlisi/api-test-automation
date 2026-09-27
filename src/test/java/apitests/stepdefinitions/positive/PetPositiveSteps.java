package apitests.stepdefinitions.positive;

import apitests.client.PetClient;
import apitests.models.Pet;
import apitests.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;

public class PetPositiveSteps {

    private final TestContext testContext;
    private final PetClient petClient = new PetClient();

    public PetPositiveSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @When("I create a pet named {string} with status {string}")
    public void iCreateAPetNamedWithStatus(String name, String status) {
        Pet pet = newPet(name, status);
        Response response = petClient.createPet(pet);
        testContext.setLastResponse(response);
        testContext.setLastCreatedPet(pet);
    }

    @Given("a pet named {string} with status {string} has been created")
    public void aPetNamedWithStatusHasBeenCreated(String name, String status) {
        iCreateAPetNamedWithStatus(name, status);
        testContext.getLastResponse().then().statusCode(200);
    }

    @When("I request that pet by its id")
    public void iRequestThatPetByItsId() {
        long id = testContext.getLastCreatedPet().getId();
        testContext.setLastResponse(petClient.getPetById(id));
    }

    @When("I update that pet's name to {string} and status to {string}")
    public void iUpdateThatPetsNameToAndStatusTo(String newName, String newStatus) {
        Pet updated = testContext.getLastCreatedPet();
        updated.setName(newName);
        updated.setStatus(newStatus);
        testContext.setLastResponse(petClient.updatePet(updated));
    }

    @When("I delete that pet")
    public void iDeleteThatPet() {
        long id = testContext.getLastCreatedPet().getId();
        testContext.setLastResponse(petClient.deletePetById(id));
    }

    @Then("requesting that pet again should return status code {int}")
    public void requestingThatPetAgainShouldReturnStatusCode(int expectedStatus) {
        long id = testContext.getLastCreatedPet().getId();
        Response response = petClient.getPetById(id);
        assertThat(response.statusCode(), equalTo(expectedStatus));
    }

    @Then("the response should match the pet schema")
    public void theResponseShouldMatchThePetSchema() {
        testContext.getLastResponse().then().assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/pet-schema.json"));
    }

    @Then("the response pet should have name {string} and status {string}")
    public void theResponsePetShouldHaveNameAndStatus(String expectedName, String expectedStatus) {
        Pet pet = testContext.getLastResponse().as(Pet.class);
        assertThat(pet.getName(), equalTo(expectedName));
        assertThat(pet.getStatus(), equalTo(expectedStatus));
    }

    @Then("requesting that pet again should return name {string} and status {string}")
    public void requestingThatPetAgainShouldReturnNameAndStatus(String expectedName, String expectedStatus) {
        long id = testContext.getLastCreatedPet().getId();
        Pet pet = petClient.getPetById(id).as(Pet.class);
        assertThat(pet.getName(), equalTo(expectedName));
        assertThat(pet.getStatus(), equalTo(expectedStatus));
    }

    @When("I search for pets with status {string}")
    public void iSearchForPetsWithStatus(String status) {
        testContext.setLastResponse(petClient.findByStatus(status));
    }

    @Then("every pet in the response should have status {string}")
    public void everyPetInTheResponseShouldHaveStatus(String expectedStatus) {
        Pet[] pets = testContext.getLastResponse().as(Pet[].class);
        assertThat(List.of(pets), everyItem(hasProperty("status", equalTo(expectedStatus))));
    }

    @When("I search for pets with tag {string}")
    public void iSearchForPetsWithTag(String tag) {
        testContext.setLastResponse(petClient.findByTags(tag));
    }

    @When("I update that pet via form data to name {string} and status {string}")
    public void iUpdateThatPetViaFormDataToNameAndStatus(String newName, String newStatus) {
        long id = testContext.getLastCreatedPet().getId();
        testContext.setLastResponse(petClient.updatePetWithForm(id, newName, newStatus));
    }

    @When("I upload an image for that pet")
    public void iUploadAnImageForThatPet() {
        long id = testContext.getLastCreatedPet().getId();
        testContext.setLastResponse(petClient.uploadImage(id, sampleImageFile()));
    }

    @When("I search for pets with statuses {string} and {string}")
    public void iSearchForPetsWithStatuses(String status1, String status2) {
        testContext.setLastResponse(petClient.findByStatus(List.of(status1, status2)));
    }

    @Then("every pet in the response should have status {string} or {string}")
    public void everyPetInTheResponseShouldHaveStatusOr(String status1, String status2) {
        Pet[] pets = testContext.getLastResponse().as(Pet[].class);
        assertThat(List.of(pets), everyItem(anyOf(
                hasProperty("status", equalTo(status1)),
                hasProperty("status", equalTo(status2)))));
    }

    @When("I search for pets with an unknown status")
    public void iSearchForPetsWithAnUnknownStatus() {
        testContext.setLastResponse(petClient.findByStatus("not_a_real_status_xyz"));
    }

    @When("I search for pets with a tag that does not exist")
    public void iSearchForPetsWithATagThatDoesNotExist() {
        testContext.setLastResponse(petClient.findByTags("no_such_tag_xyz"));
    }

    @Then("the response should be an empty list")
    public void theResponseShouldBeAnEmptyList() {
        Pet[] pets = testContext.getLastResponse().as(Pet[].class);
        assertThat(pets, emptyArray());
    }

    // PUT on an id that was never created still returns 200 and stores the pet -
    // this server treats /pet updates as an upsert rather than requiring a prior create.
    @When("I update a pet id that was never created with name {string} and status {string}")
    public void iUpdateAPetIdThatWasNeverCreatedWithNameAndStatus(String name, String status) {
        Pet pet = newPet(name, status);
        testContext.setLastResponse(petClient.updatePet(pet));
        testContext.setLastCreatedPet(pet);
    }

    private File sampleImageFile() {
        URL resource = getClass().getClassLoader().getResource("uploads/sample-photo.png");
        if (resource == null) {
            throw new IllegalStateException("Test fixture uploads/sample-photo.png not found on classpath");
        }
        return new File(resource.getFile());
    }

    private Pet newPet(String name, String status) {
        long id = uniquePetId();
        return Pet.builder()
                .id(id)
                .name(name)
                .status(status)
                .category(1, "test-category")
                .photoUrls(List.of("https://example.com/photo.jpg"))
                .tags(List.of())
                .build();
    }

    // The Petstore demo server is shared by everyone running this suite; a random,
    // high-range id keeps concurrent runs from colliding on the same pet record.
    private long uniquePetId() {
        return ThreadLocalRandom.current().nextLong(100_000_000L, 999_999_999L);
    }
}
