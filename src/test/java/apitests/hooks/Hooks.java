package apitests.hooks;

import apitests.client.PetClient;
import apitests.config.ApiConfig;
import apitests.support.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class Hooks {

    private final TestContext testContext;
    private final PetClient petClient = new PetClient();

    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = ApiConfig.baseUri();
    }

    // Best-effort cleanup so scenarios don't leave test data behind on the shared demo server.
    // A pet already removed by the scenario itself (e.g. the delete test) simply 404s here, which is fine.
    @After
    public void cleanUp() {
        if (testContext.getLastCreatedPet() != null && testContext.getLastCreatedPet().getId() != null) {
            petClient.deletePetById(testContext.getLastCreatedPet().getId());
        }
    }
}
