package apitests.stepdefinitions.negative;

import apitests.client.PetClient;
import apitests.support.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PetNegativeSteps {

    private final TestContext testContext;
    private final PetClient petClient = new PetClient();

    public PetNegativeSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @When("I request a pet with id {long}")
    public void iRequestAPetWithId(long id) {
        testContext.setLastResponse(petClient.getPetById(id));
    }

    @When("I delete a pet with id {long}")
    public void iDeleteAPetWithId(long id) {
        testContext.setLastResponse(petClient.deletePetById(id));
    }

    @When("I send a malformed create pet request")
    public void iSendAMalformedCreatePetRequest() {
        testContext.setLastResponse(petClient.createPetWithRawBody("{not-valid-json"));
    }

    @Then("the response body message should be {string}")
    public void theResponseBodyMessageShouldBe(String expectedMessage) {
        String actualMessage = testContext.getLastResponse().jsonPath().getString("message");
        assertThat(actualMessage, equalTo(expectedMessage));
    }
}
