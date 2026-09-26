package apitests.stepdefinitions.negative;

import apitests.client.PetClient;
import apitests.support.TestContext;
import io.cucumber.java.en.When;

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

    @When("I update the pet with id {long} via form data to name {string} and status {string}")
    public void iUpdateThePetWithIdViaFormDataToNameAndStatus(long id, String name, String status) {
        testContext.setLastResponse(petClient.updatePetWithForm(id, name, status));
    }

    @When("I request a pet with a non-numeric id")
    public void iRequestAPetWithANonNumericId() {
        testContext.setLastResponse(petClient.getPetById("notanumber"));
    }

    @When("I delete a pet with a non-numeric id")
    public void iDeleteAPetWithANonNumericId() {
        testContext.setLastResponse(petClient.deletePetById("notanumber"));
    }
}
