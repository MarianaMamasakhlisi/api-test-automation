package apitests.stepdefinitions;

import apitests.client.PetClient;
import apitests.models.Pet;
import apitests.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class PetCrudSteps {

    private final TestContext testContext;
    private final PetClient petClient = new PetClient();

    public PetCrudSteps(TestContext testContext) {
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

    @When("I request a pet with id {long}")
    public void iRequestAPetWithId(long id) {
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

    @When("I delete a pet with id {long}")
    public void iDeleteAPetWithId(long id) {
        testContext.setLastResponse(petClient.deletePetById(id));
    }

    @When("I send a malformed create pet request")
    public void iSendAMalformedCreatePetRequest() {
        testContext.setLastResponse(petClient.createPetWithRawBody("{not-valid-json"));
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatus) {
        assertThat(testContext.getLastResponse().statusCode(), equalTo(expectedStatus));
    }

    @Then("requesting that pet again should return status code {int}")
    public void requestingThatPetAgainShouldReturnStatusCode(int expectedStatus) {
        long id = testContext.getLastCreatedPet().getId();
        Response response = petClient.getPetById(id);
        assertThat(response.statusCode(), equalTo(expectedStatus));
    }

    @Then("the response content type should be {string}")
    public void theResponseContentTypeShouldBe(String expectedContentType) {
        assertThat(testContext.getLastResponse().contentType(), containsString(expectedContentType));
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

    @Then("the response body message should be {string}")
    public void theResponseBodyMessageShouldBe(String expectedMessage) {
        String actualMessage = testContext.getLastResponse().jsonPath().getString("message");
        assertThat(actualMessage, equalTo(expectedMessage));
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
