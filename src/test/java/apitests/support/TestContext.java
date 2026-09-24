package apitests.support;

import apitests.models.Pet;
import io.restassured.response.Response;

/**
 * Scenario-scoped state shared between step definitions and hooks.
 * Cucumber creates a fresh instance (via picocontainer) for every scenario.
 */
public class TestContext {

    private Response lastResponse;
    private Pet lastCreatedPet;

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }

    public Pet getLastCreatedPet() {
        return lastCreatedPet;
    }

    public void setLastCreatedPet(Pet lastCreatedPet) {
        this.lastCreatedPet = lastCreatedPet;
    }
}
